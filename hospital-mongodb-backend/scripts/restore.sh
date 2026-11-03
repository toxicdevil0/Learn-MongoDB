#!/usr/bin/env sh
set -eu
[ -n "${1:-}" ] || { echo "Usage: sh scripts/restore.sh <backup-folder>"; exit 1; }
mongorestore --uri="${MONGODB_URI:-mongodb://localhost:27017/hospital_management}" --drop "$1" 