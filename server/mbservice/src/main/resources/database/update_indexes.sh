#!/bin/bash

[[ ! $1 ]] && {
    echo "Usage: $0 <schema-file>";
    exit 1;
}

ifile="$1";

[ ! -f "$ifile" ] && {
 echo "File not found $ifile";
 exit 2;
}

echo "set schema 'musicbrainz';" > mbdb_indexes.sql
grep -e '^CREATE INDEX' $ifile >> mbdb_indexes.sql
exit 0;
