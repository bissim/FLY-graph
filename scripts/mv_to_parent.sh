#! /bin/bash

# strict mode
set -euo pipefail
IFS=$'\n\t'

echo "Running $0..."

if (( $# <= 0 )); then
    echo
    echo "Cannot run $0 unless a target directory is specified."
    echo "Syntax: $0 <target_directory>"
    echo "<target_directory> must end with '/' character"
    exit 1
fi

TARGET=$1
PARENT=${TARGET%/*}
PARENT=${PARENT%/*}/

# Nullity check
if [[ ! -e $TARGET ]]; then
    echo
    echo "$TARGET directory doesn't exist!"
    exit 1
fi

TARGET_FILES=$(ls $TARGET)

# Empty check
if [[ -z $TARGET_FILES ]]; then
    echo
    echo "There are no files in $TARGET!"
    exit 1
fi

echo
echo "Target directory is $TARGET"
echo "Files in directory: $TARGET_FILES"
echo "Target directory parent is $PARENT"

# Move from target directory to current one
echo
echo "Files in parent directory after running $0: $(ls $PARENT)"

for item in $TARGET_FILES; do
    mv $TARGET/$item $PARENT
done
rm -r $TARGET

echo
echo "Files in directory after running $0: $(ls $PARENT)"
