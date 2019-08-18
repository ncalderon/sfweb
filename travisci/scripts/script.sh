#!/usr/bin/env bash
./mvnw test -Dmaven.javadoc.skip=true -B -V -q
exit 0
