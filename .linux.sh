#!/bin/sh

if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then 
  echo Starting dbus
  eval $(/usr/bin/dbus-launch --sh-syntax); 
  echo Starting gnome-keyring-daemon
  eval $(printf password|gnome-keyring-daemon --login)
  echo Logging in to gnome-keyring-daemon
  python -c "import gnomekeyring;gnomekeyring.unlock_sync(None, 'password');"

  echo Storing a secret with secret-tool
  printf test|secret-tool store --label='Password for mydrive' drive mydrive
  PASS=`secret-tool lookup drive mydrive`

  echo Checking the secret can be retrieved.
  if  [ "$PASS" != "test" ]; then
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

