#!/bin/bash

repo="https://github.com/slaszu/ktor-gpw-api/releases/download/TAG/ktor-gpw-api"

echo "Add tag name as argument"

repo=${repo/TAG/"$1"}

cmd="curl -L $repo -o ./ktor-gpw-api-github-release"

echo "Command: $cmd"
eval $cmd

chmod="chmod +x ./ktor-gpw-api-github-release"
echo "Command: $chmod"
eval $chmod