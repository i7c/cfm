#!/bin/bash

[[ ! $6 ]] && {
        echo "Usage: $0 <host> <port> <dbname> <user> <password> <dumps ...>";
        exit 1;
}

host="$1"
port="$2"
db="$3"
user="$4"
pw="$5"
shift 5

echo "----- STAGE 1 -----"
./setup_ec2.sh "$host" "$port" "$db" "$user" "$pw" || exit 1;
echo "----- STAGE 2 -----"
./psql_initial.sh "$host" "$port" "$db" "$user" || exit 2;
echo "----- STAGE 3 -----"
./create_schemas.sh || exit 3;
echo "----- STAGE 4 -----"
./import_data.sh "$@" || exit 4;
echo "----- STAGE 5 -----"
./create_indexes.sh || exit 5;

echo "DONE"
exit 0
