FROM docker:latest

# Install docker-compose
RUN apk add  docker-cli-compose

# Set working directory
WORKDIR /app

# Copy the docker-compose file
COPY docker-compose.yml ./

# Start the containers in detached mode
CMD ["docker-compose", "up", "-d"]
