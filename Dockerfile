FROM docker:dind

# Install Docker Compose
RUN apk add --no-cache py3-pip && \
    pip3 install docker-compose

# Set the working directory to where the docker-compose.yml file will be
WORKDIR /app

# Copy your docker-compose.yml file into the container
COPY docker-compose.yml .

# Run docker-compose inside the container
CMD ["docker-compose", "up"]
