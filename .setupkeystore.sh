#!/bin/bash

set -x
if [[ "$(expr substr $(uname -s) 1 5)" == "Linux" ]]; then 
  export $(dbus-launch)
  eval "$(echo '\n' | gnome-keyring-daemon --unlock)"
  echo xxx@gmail.com | secret-tool store --label="main" email address
  PASS=$(secret-tool lookup email address)

  echo Checking the secret can be retrieved.
  if  [[ "$PASS" != "xxx@gmail.com" ]]; then
    echo FAIL: the secret could not be retrieved.
    return 1;
  fi
  echo SUCCESS: the secret could not be retrieved.
fi 

if [[ "$(uname -s)" == "Darwin" ]]; then 
  ls -la ~/Library/Keychains/
  #rm -rf ~/Library/Keychains/login.keychain
fi

