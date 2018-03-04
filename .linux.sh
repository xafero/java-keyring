#!/bin/sh

if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then 
  eval $(/usr/bin/dbus-launch --sh-syntax); 
  echo "password" | gnome-keyring-daemon --unlock;
  echo DBUS_SESSION_BUS_ADDRESS=$DBUS_SESSION_BUS_ADDRESS;
  echo DBUS_SESSION_BUS_PID=$DBUS_SESSION_BUS_PID;
fi 