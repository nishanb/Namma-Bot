# Use the official Node.js 14 image as the base image
FROM node:14

# Set the working directory in the container
WORKDIR /app

# Install the Mockoon CLI globally
RUN npm install -g @mockoon/cli

# Copy the mockon.json file into the container
COPY Namma-Yathri-API.json mockon.json

# Expose port 80
EXPOSE 80

# Start the Mockoon server when the container starts
CMD ["mockoon-cli" , "start", "--data", "mockon.json", "--port", "80", "-D"]
