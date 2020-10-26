#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

printf "Running %s...\n" $0

# move to data folder
cd $HOME/git/FLY-graph/dataset/

SOURCES_LIST=$(ls | grep ".txt$")
#SOURCES_LIST=$(echo $(ls | grep ".txt$") | tr "\n" "\t")
#SOURCES_LIST=$(echo -e "facebook_combined.txt\nsoc-Epinions1.txt\nsoc-Slashdot0811.txt\nsoc-Slashdot0902.txt\nwiki-Vote.txt")
#SOURCES_LIST="test.txt" # TODO remove
echo "Sources: $SOURCES_LIST"

# normalize sources
for source in $SOURCES_LIST; do
  echo "Cleaning source: $source"

  # remove comments
  sed -i '/^#/d' $source

  # remove self loops
  echo "$(awk '$1 != $2' $source)" > $source

  # replace tabs with spaces
  echo "$(tr "\t" " " < $source)" > $source
done

