#!/bin/bash

[[ ! $8 ]] && {
        echo "Usage: $0 <host> <port> <dbname> <user> <password> <setup script> <mbslave location> <dumps ...>";
        exit 1;
}

host="$1"
export host
port="$2"
export port
db="$3"
export db
user="$4"
export user
pw="$5"
export pw
setup="$6"
export setup
mbsl="$7"
export mbsl
shift 7

echo "----- STAGE 1 -----"
$setup $mbsl

echo "----- STAGE 2 -----"
./psql_initial.sh || exit 2;

echo "----- STAGE 3 -----"
./create_schemas.sh || exit 3;

echo "----- STAGE 4 -----"
./import_data.sh "$@" || exit 4;

echo "----- STAGE 5 -----"
./create_indexes.sh || exit 5;

echo "DONE"
exit 0
