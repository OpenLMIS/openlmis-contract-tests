#!/usr/bin/env bash

set -e

# ensure some environment variables are set
: "${DATABASE_URL:?DATABASE_URL not set in environment}"
: "${POSTGRES_USER:?POSTGRES_USER not set in environment}"
: "${POSTGRES_PASSWORD:?POSTGRES_PASSWORD not set in environment}"

# pull apart some of those pieces stuck together in DATABASE_URL

URL=`echo ${DATABASE_URL} | sed -E 's/^jdbc\:(.+)/\1/'` # jdbc:<url>
: "${URL:?URL not parsed}"

HOST=`echo ${DATABASE_URL} | sed -E 's/^.*\/{2}(.+):.*$/\1/'` # //<host>:
: "${HOST:?HOST not parsed}"

PORT=`echo ${DATABASE_URL} | sed -E 's/^.*\:([0-9]+)\/.*$/\1/'` # :<port>/
: "${PORT:?Port not parsed}"

DB=`echo ${DATABASE_URL} | sed -E 's/^.*\/(.+)\?*$/\1/'` # /<db>?
: "${DB:?DB not set}"

export PGHOST=$HOST
export PGPORT=$PORT
export PGDATABASE=$DB
export PGUSER=$POSTGRES_USER
export PGPASSWORD=$POSTGRES_PASSWORD

# Create baseline if it does not already exist
# ideal_stock_amounts excluded because of bug described in OLMIS-3341
if [ ! -f open_lmis.dump ]; then
  pg_dump --exclude-table-data=referencedata.ideal_stock_amounts -Fc open_lmis > open_lmis.dump
fi

# Restore from baseline, after disconnecting and cleaning up db
psql -q -t -c "ALTER DATABASE open_lmis CONNECTION LIMIT 0;" -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'open_lmis';" postgres >/dev/null
dropdb open_lmis
createdb open_lmis
pg_restore -d open_lmis open_lmis.dump