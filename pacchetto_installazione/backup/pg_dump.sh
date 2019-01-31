#!/bin/bash

#set -x

DIR=/tomcat/DUMP_DB

#DATE=`date "+%Y%m%d"`
day=`date "+%a"`
BZIP2=/usr/bin/bzip2
PG_DUMP=/bin/pg_dump
THISHOST=`uname -n`

su - postgres -c "${PG_DUMP} opacDB -U postgres| ${BZIP2} -9c > ${DIR}/${THISHOST}.${day}.opacDB.bzdump"

cd ${DIR}

md5sum ${THISHOST}.${day}.opacDB.bzdump > ${THISHOST}.${day}.opacDB.bzdump.md5
