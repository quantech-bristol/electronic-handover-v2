#!/bin/bash
psql postgres <<END_OF_SQL
DROP DATABASE handoverdb;
CREATE DATABASE handoverdb;
GRANT ALL PRIVILEGES ON DATABASE handoverdb TO quantech;

