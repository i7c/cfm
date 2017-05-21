#!/bin/bash

[ ! -d  "$mbsl" ] && {
	echo "Missing mbslave.";
	exit 1;
}

[[ ! $1 ]] && {
	echo "Usage: $0 <dumpfiles ...>";
	exit 2;
}

cd "$mbsl" || { echo "Failed."; exit 3; }

./mbslave-import.py "$@"

