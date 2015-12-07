#!/bin/bash

ps -ef | grep mongodb | grep -v grep | awk '{print $2}' | xargs kill -9