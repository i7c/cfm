#!/bin/bash

echo "Might prompt for database password ..."
psql -h "$host" -p "$port" -U "$user" -c 'CREATE EXTENSION cube;' $db || true
echo "Might prompt for database password ..."
psql -h "$host" -p "$port" -U "$user" -c 'CREATE EXTENSION earthdistance;' $db || true
