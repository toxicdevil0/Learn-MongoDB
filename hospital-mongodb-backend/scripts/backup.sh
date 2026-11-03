#!/usr/bin/env sh
set -eu
mkdir -p backups
STAMP=$(date +%Y%m%d_%H%M%S)
mongodump --uri="${MONGODB_URI:-mongodb://localhost:27017/hospital_management}" --out="backups/$STAMP"
echo "Backup saved: backups/$STAMP" 