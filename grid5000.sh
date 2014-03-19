#!/bin/sh
cd /root/diversify-statements
git pull
mvn clean package

rm -rf repo/sosie-exp
mkdir repo
sh script/git/init.sh repo

sleep $((RANDOM%700))

java -jar target/Diversify-statements-1.0-SNAPSHOT-jar-with-dependencies.jar git repo

sh runFromGit.sh 100 &
