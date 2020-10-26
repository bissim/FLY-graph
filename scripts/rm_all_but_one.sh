#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

echo "Running $0..."

if (( $# <= 1 )); then
    echo
    echo "Cannot run $0 unless a target directory and an orphan file are specified."
    echo "Syntax: $0 <target_directory> <orphan_file>"
    echo "<target_directory> must end with '/' character"
    exit 1
fi

TARGET=$1
ORPHAN=$2
TARGET_FILES=$(ls $TARGET)

echo "Target directory is $TARGET, orphan file is $ORPHAN"

echo
echo "Files in directory: $TARGET_FILES"

# Nullity check
if [[ -z $TARGET_FILES ]]; then
    echo
    echo "There are no files in $TARGET!"
    exit 1
fi

# Orphan feasibility check
if echo "$TARGET_FILES" | grep -q "$ORPHAN"; then
    echo
    echo "$ORPHAN is in $TARGET directory"
else
    echo "$ORPHAN is NOT in $TARGET directory!"
    exit 1
fi

# Singularity check
if [[ $TARGET_FILES == $ORPHAN ]]; then
    echo
    echo "$ORPHAN is already the only file in $TARGET!"
    exit 1
fi

for item in $TARGET_FILES; do
    if [[ ! $item == $ORPHAN ]]; then
        FILE_PATH="$TARGET$item"
        if [[ ! -e $FILE_PATH ]]; then
            echo "Path $FILE_PATH is invalid!"
            exit 1
        fi

        echo "Deleting $FILE_PATH..."
        if [[ -d $FILE_PATH ]]; then
            rm -r $FILE_PATH
            echo "Deleted $item directory from $TARGET"
        else
            rm $FILE_PATH
            echo "Deleted $item file from $TARGET"
        fi
    fi
done

echo
echo "Files in directory after running $0: $(ls $TARGET)"
