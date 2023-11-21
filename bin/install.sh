#!/bin/bash

exec mvn -T 1C clean source:jar  install -pl api,core,common,springboot -Dmaven.test.skip=false
