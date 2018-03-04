#!/bin/sh

if [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then 
  eval $(/usr/bin/dbus-launch --sh-syntax); 
  gnome-keyring-daemon with --unlock;
  echo DBUS_SESSION_BUS_ADDRES=$DBUS_SESSION_BUS_ADDRES;
  echo DBUS_SESSION_BUS_PID=$DBUS_SESSION_BUS_PID;
fi 