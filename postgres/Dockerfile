FROM postgres:16

# Copy the init-db.sh script to the PostgreSQL container
COPY init-db.sh /docker-entrypoint-initdb.d/

# Make sure the script is executable
RUN chmod +x /docker-entrypoint-initdb.d/init-db.sh