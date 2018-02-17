#!/bin/bash

sudo -u postgres pg_dump --schema-only cfm 2> /dev/null > ../src/main/resources/db/schema.sql
