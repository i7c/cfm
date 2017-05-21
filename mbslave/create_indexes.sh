#!/bin/bash

[ ! -d  "$mbsl" ] && {
	echo "Missing mbslave.";
	exit 1;
}

cd "$mbsl" || { echo "Failed."; exit 2; }

./mbslave-remap-schema.py <sql/CreatePrimaryKeys.sql | ./mbslave-psql.py
./mbslave-remap-schema.py <sql/statistics/CreatePrimaryKeys.sql | ./mbslave-psql.py
./mbslave-remap-schema.py <sql/caa/CreatePrimaryKeys.sql | ./mbslave-psql.py
./mbslave-remap-schema.py <sql/wikidocs/CreatePrimaryKeys.sql | ./mbslave-psql.py
./mbslave-remap-schema.py <sql/documentation/CreatePrimaryKeys.sql | ./mbslave-psql.py

./mbslave-remap-schema.py <sql/CreateIndexes.sql | grep -v musicbrainz_collate | ./mbslave-psql.py
./mbslave-remap-schema.py <sql/CreateSlaveIndexes.sql | ./mbslave-psql.py
./mbslave-remap-schema.py <sql/statistics/CreateIndexes.sql | ./mbslave-psql.py
./mbslave-remap-schema.py <sql/caa/CreateIndexes.sql | ./mbslave-psql.py

./mbslave-remap-schema.py <sql/CreateViews.sql | ./mbslave-psql.py
