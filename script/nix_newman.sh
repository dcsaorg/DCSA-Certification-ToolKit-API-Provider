#!/bin/sh

# Example run-folder.sh env.json collection.json "My Folder"

#echo "Generating Temporary Environment"
#newman run "${2}" --environment "${1}" --silent --export-env /tmp/temp-newman-env.json --folder "Fixtures"
#newman run "${2}" --environment /tmp/temp-newman-env.json --bail --reporters cli --folder "${3}"

newman run ./Provider_CTK.postman_collection.json