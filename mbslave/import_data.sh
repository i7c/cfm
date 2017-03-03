#!/bin/bash

dir="$HOME/git/mbslave"
[ ! -d  "$dir" ] && {
	echo "Missing mbslave. Maybe you have to run setup_ec2.sh first.";
	exit 1;
}

[[ ! $1 ]] && {
	echo "Usage: $0 <dumpfiles ...>";
	exit 2;
}

cd "$dir" || { echo "Failed."; exit 2; }

./mbslave-import.py "$@"

