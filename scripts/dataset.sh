#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

SOURCE="sources.list"
SOURCE_DIR="$HOME/git/FLY-graph/dataset/"
S3_DIR="https://fly-graph-data.s3.amazonaws.com/"
#DATASET="dataset.list"
DATASET_ROWS=64
#EXT=".edges"
SOURCES_COUNT=0

# read filename from command line
if [[ $# -ge 1 ]]; then
  printf "Running %s...\n" $0
  FILENAME=$1
  if [[ ! -e $SOURCE_DIR$FILENAME ]]; then
    printf "%s file doesn't exist!\n" $FILENAME
    exit -1
#  else
#    printf "%s file exists!\n" $FILENAME
#    exit 0
  fi
else
  printf "Specify source path as argument!\n"
  exit -1
fi

# move to dataset folder
cd $SOURCE_DIR

# declare sources probability map
#declare -A SOURCE_P

# declare sources array
SOURCE_A=()

# download graphs from source file
for URL in $(cat $SOURCE); do
  ARCHIVE_NAME=${URL##*/}

  # insert archive name into probabilty map
#  SOURCE_P[$ARCHIVE_NAME]=""
  # insert archive name into array
  SOURCE_A[SOURCES_COUNT++]=$ARCHIVE_NAME

#  printf "Downlading %s...\n" $ARCHIVE_NAME
  # download graph archives
  #URL=http://snap.stanford.edu/data/facebook.tar.gz
#  wget -q $URL

  # extract archive name
#  printf "Extracting %s...\n" $ARCHIVE_NAME

  # extract only edgelists from archives
#  gzip -qd $ARCHIVE_NAME #--wildcards **/*$EXT

  # remove archives
  #rm $ARCHIVE_NAME
done

# attribute probability to each source
#printf "Number of sources: %d\n" ${#SOURCE_P[@]}
#echo "Sources: ${!SOURCE_P[@]}"
#echo "Sources: ${SOURCE_A[@]}"
#echo "Sources keys: ${!SOURCE_A[@]}"
#for key in ${!SOURCE_P[@]}; do
#  SOURCE_P[$key]=$(bc <<< "scale=2;1.0/${#SOURCE_P[@]}")
#  SOURCE_P[$key]=.13
#done
#echo "Probabilities: ${SOURCE_P[@]}"
#printf "Probabilities: %s\n" "${SOURCE_P[@]}"

# assign lower probability to heavier sources
#SOURCE_P[gplus_combined.txt.gz]=.02
#SOURCE_P[soc-LiveJournal.txt.gz]=.02
#SOURCE_P[soc-pokec-relationships.txt.gz]=.05
#for key in ${!SOURCE_P[@]}; do
#  printf "%s has probability %s\n" "$key" "${SOURCE_P[$key]}"
#done

# define dataset file with random-picked sources
#AVG_COUNT=$(($DATASET_ROWS*80/100))
#HEAVY_COUNT=$(($DATASET_ROWS*20/100+1))
#printf "Extracting %d times from average-sized sources\n" $AVG_COUNT
#printf "Extracting %d times from heavy-sized sources\n" $HEAVY_COUNT
#unset SOURCE_P[gplus_combined.txt.gz]
#unset SOURCE_P[soc-LiveJournal1.txt.gz]
#unset SOURCE_P[soc-pokec-relationships.txt.gz]

# remove heavy sized sources from array
#SOURCE_A=( ${SOURCE_A[@]/gplus_combined*/} )
#SOURCE_A=( ${SOURCE_A[@]/soc-LiveJournal1*/} )
#SOURCE_A=( ${SOURCE_A[@]/soc-pokec-relationships*/} )
#SOURCE_A=( ${SOURCE_A[@]/twitter_combined*/} )
#SOURCE_A=( ${SOURCE_A[@]/soc-Slashdot0811*/} )
#SOURCE_A=( ${SOURCE_A[@]/soc-Slashdot0902*/} )
#SOURCE_A=( ${SOURCE_A[@]/wiki-Vote*/} ) # not a DAG
#SOURCE_A=( ${SOURCE_A[@]/soc-Epinions1*/} ) # not a DAG
#echo "Sources with no heavy sources: ${SOURCE_A[@]}"
#H_SOURCE_A=("gplus_combined.txt.gz" "soc-LiveJournal1.txt.gz" "soc-pokec-relationships.txt.gz")
#H_INDEXES=()
#for i in ${!SOURCE_A[@]}; do
#  echo "i is $i"
#  if "${SOURCE_A[$i]}" in "${H_SOURCE_A[@]}"; then
#    H_INDEXES[${#H_INDEXES[@]}]=$i
#  fi
#done
#echo "H_INDEXES: ${H_INDEXES[@]}"
#DATASET_DIR=""
DATASET_FILE=""
COUNT=1000
while (( $COUNT <= 4000 )); do
#  DATASET_DIR=$COUNT"-"${FILENAME%*.txt}
  DATASET_FILE=$COUNT"-"${FILENAME%*.txt}".list"
  if [[ -e $DATASET_FILE ]]; then
    rm $DATASET_FILE
  fi
  touch $DATASET_FILE
#  if [[ -e $DATASET_DIR ]]; then
#    rm -r $DATASET_DIR
#  fi
  printf "Generating %s...\n" $DATASET_FILE
#  mkdir $DATASET_DIR
#  printf "" > $COUNT"-"$DATASET
#  RND_INDEX=0
#  FILENAME=""
#  for ((i = 0; i < $AVG_COUNT; i++)); do # 80% of rows are average size sources
  for ((i = 0; i < $COUNT; i++)); do
#    RND_INDEX=$(($RANDOM % ${#SOURCE_A[@]}))
#    FILENAME=${SOURCE_A[$RND_INDEX]%*.gz}
#    FILENAME="facebook_combined.txt"
#    printf "Filename: %s\n" $FILENAME
#    echo "${SOURCE_A[$RND_INDEX]}" >> $COUNT"-"$DATASET
#    printf "%s%s\n" $SOURCE_DIR $FILENAME >> $DATASET_FILE
    printf "%s%s\n" $S3_DIR $FILENAME >> $DATASET_FILE
    # create symbolic link to source file
#    ln -s $SOURCE_DIR/$FILENAME $SOURCE_DIR/$DATASET_DIR/$i"-"$FILENAME
  done
#  for ((i = 0; i < $HEAVY_COUNT; i++)); do # 20% of rows are heavy size sources
#    RND_INDEX=$(($RANDOM % ${#H_SOURCE_A[@]}))
#    echo "${H_SOURCE_A[$RND_INDEX]}" >> $COUNT"-"$DATASET
#  done

  # shuffle dataset lines
#  echo "$(shuf $COUNT"-"$DATASET)" > $COUNT"-"$DATASET
#  echo "$(tr " " "\n" < $COUNT"-"$DATASET)" > $COUNT"-"$DATASET
  COUNT=$(($COUNT + 1000))
done
