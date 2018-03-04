#!/bin/sh

if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then 
  eval $(/usr/bin/dbus-launch --sh-syntax); 
  export `/usr/bin/gnome-keyring-daemon`
  echo DBUS_SESSION_BUS_ADDRESS=$DBUS_SESSION_BUS_ADDRESS;
  echo DBUS_SESSION_BUS_PID=$DBUS_SESSION_BUS_PID;
  echo GNOME_KEYRING_CONTROL=$GNOME_KEYRING_CONTROL;
  echo SSH_AUTH_SOCK=$SSH_AUTH_SOCK;
  echo GPG_AGENT_INFO=$GPG_AGENT_INFO;
  echo GNOME_KEYRING_PID=$GNOME_KEYRING_PID;
fi 