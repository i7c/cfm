#!/bin/bash

[[ ! $5 ]] && {
	echo "Usage: $0 <host> <port> <dbname> <user> <password>";
	exit 1;
}

host="$1"
port="$2"
db="$3"
user="$4"
pw="$5"

sudo yum update
sudo yum install git tmux vim
sudo yum install python python-psycopg2 python-psycopg2 python27-psycopg2
sudo yum install postgresql-libs postgresql psql
sudo yum install buildutils coreutils gcc

mkdir -p ~/git
git clone 'https://github.com/lalinsky/mbslave.git' ~/git/mbslave

target="$HOME/git/mbslave/mbslave.conf"
cp ~/git/mbslave/mbslave.conf.default "$target"

sed -i 's/^host=.*$/host='"$host"'/' $target
sed -i 's/^port=.*$/port='"$port"'/' $target
sed -i 's/^name=.*$/name='"$db"'/' $target
sed -i 's/^user=.*$/user='"$user"'/' $target
sed -i 's/^#password=.*$/password='"$pw"'/' $target
