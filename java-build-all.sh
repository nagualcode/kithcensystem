#!/bin/bash

for dir in *; do
	if cd "$dir" 2>/dev/null; then
		if ls pom.xml 2>/dev/null; then
			pwd
			mvn clean install
			ls target
		fi
		cd ..
	fi
done
