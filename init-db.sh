# init-db.sh
set -e

# Create the second database
psql -U $POSTGRES_USER -d $POSTGRES_DB -c "CREATE DATABASE login"

echo "DB CREATED"