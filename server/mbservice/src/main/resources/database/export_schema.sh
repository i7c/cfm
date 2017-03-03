#!/bin/bash

[[ ! $3 ]] && {
    echo "Usage: $0 <host> <port> <user>";
    exit 1;
}

host="$1";
port="$2";
user="$3";
schema="musicbrainz";
db="mbdb";

pg_dump -v -h "$host" -p "$port" -U "$user" -n "$schema" -s "$db"
