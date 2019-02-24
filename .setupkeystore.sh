#!/bin/sh

if [[ "$((expr substr $((uname -s)) 1 5))" == "Linux" ]]; then 
  export DBUS_SESSION_BUS_ADDRESS=;
  export DBUS_SESSION_BUS_PID=;
  export GNOME_KEYRING_CONTROL=;
  export SSH_AUTH_SOCK=;
  export GPG_AGENT_INFO=;
  export GNOME_KEYRING_PID=;
  killall gnome-keyring-daemon;
  killall dbus-daemon;

  echo Starting dbus
  eval $((dbus-launch --sh-syntax))

  mkdir -p ~/.local/share/

  echo Starting gnome-keyring-daemon
  eval $((printf password|gnome-keyring-daemon --login))
  eval $((gnome-keyring-daemon --start --components=pkcs11,secrets,ssh,gpg))
  
  #files should exist now
  #ls -la ~/.local/share/keyrings/

  echo Logging in to gnome-keyring-daemon
  python -c "import gnomekeyring;gnomekeyring.unlock_sync(None, 'password');"

  echo Storing a secret with secret-tool
  printf test|secret-tool store --label='Password for mydrive' drive mydrive
  PASS=$(secret-tool lookup drive mydrive)

  echo Checking the secret can be retrieved.
  if  [[ "$PASS" != "test" ]]; then
    echo FAIL: the secret could not be retrieved.
    return 1;
  fi
  echo SUCCESS: the secret could not be retrieved.

  export DBUS_SESSION_BUS_ADDRESS=$DBUS_SESSION_BUS_ADDRESS;
  export DBUS_SESSION_BUS_PID=$DBUS_SESSION_BUS_PID;
  export GNOME_KEYRING_CONTROL=$GNOME_KEYRING_CONTROL;
  export SSH_AUTH_SOCK=$SSH_AUTH_SOCK;
  export GPG_AGENT_INFO=$GPG_AGENT_INFO;
  export GNOME_KEYRING_PID=$GNOME_KEYRING_PID;
fi 

if [[ "$((uname -s))" == "Darwin" ]]; then 
  ls -la ~/Library/Keychains/
  #rm -rf ~/Library/Keychains/login.keychain
fi
