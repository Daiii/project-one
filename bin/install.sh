#!/bin/bash

exec mvn -T 1C clean source:jar  install -Dmaven.test.skip=false
