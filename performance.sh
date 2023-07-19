#!/usr/bin/env bash

gradle-profiler --benchmark \
  --warmups 2 \
  --scenario-file performance.scenarios copy-do-not-track copy