#!/bin/bash

[[ ! $4 ]] && {
	echo "Usage: $0 <host> <port> <dbname> <user>";
	exit 1;
}

host="$1"
port="$2"
db="$3"
user="$4"

echo "Will prompt for database password ..."
psql -h "$host" -p "$port" -U "$user" -c 'CREATE EXTENSION cube;'
echo "Will prompt for database password ..."
psql -h "$host" -p "$port" -U "$user" -c 'CREATE EXTENSION earthdistance;'
