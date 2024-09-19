# Use an official node image as the base image
FROM node:16

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and yarn.lock files
COPY package*.json ./

# Install the dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the port the app runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
