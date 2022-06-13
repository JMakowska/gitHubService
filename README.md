# GitHubService
Simple REST service that retrieves and processes data from GitHub created for the purposes of the recruitment process.

### Run application as Docker container
To run application as Docker container, you should run the following commands from the project directory level. By default, the application listens on port 8080.

`docker build -f Dockerfile -t github-service .`

`docker run -d --name github-service -p 8080:8080 github-service .`

### Swagger UI
After starting the application, the REST api documentation is available at http://localhost:8080/swagger-ui/index.html
