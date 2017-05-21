#!/bin/bash

echo "Asking sudo password for installing dependencies with pacman ...";
sudo pacman --needed -S git tmux python2 python2-psycopg2 coreutils gcc || exit 2

[ -e "$mbsl" ] && {
    echo "Path already exists. Aborting."
    exit 3;
}
mkdir -p "$mbsl"
git clone 'https://github.com/lalinsky/mbslave.git' "$mbsl" || exit 3

cd "$mbsl" || exit 3
sed -i 's/env python/env python2/' *.py

target="$mbsl/mbslave.conf"
cp "$target.default" "$target"

sed -i 's/^host=.*$/host='"$host"'/' $target
sed -i 's/^port=.*$/port='"$port"'/' $target
sed -i 's/^name=.*$/name='"$db"'/' $target
sed -i 's/^user=.*$/user='"$user"'/' $target
sed -i 's/^#password=.*$/password='"$pw"'/' $target
