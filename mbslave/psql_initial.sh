#!/bin/bash

[[ ! $4 ]] && {
	echo "Usage: $0 <host> <port> <dbname> <user>";
	exit 1;
}

host="$1"
port="$2"
db="$3"
user="$4"

psql -h "$host" -p "$port" -U "$user" -c 'CREATE EXTENSION cube;'
psql -h "$host" -p "$port" -U "$user" -c 'CREATE EXTENSION earthdistance;'
