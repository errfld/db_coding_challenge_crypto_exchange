# Deutsche Bank Coding Challenge

After the simplest solution to be found in branch [1_simple_solution](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/1_simple_solution) I started with CI/CD and created a simple pipeline based on github actions.
While github action are new to me, as I typically use gitlab, best-practices were no concern for this challenge. So this has already be a win, I learned something new.

## What changed?

Till this step frontend and backend had to be started by either `yarn dev` in case of the frontend or `mvn spring-boot:run` in case of the backend, and this is absolutly fine for development, but we have to pave the road to production.
I assume a container environment compatible with OCI Containers, thus I created simple `Dockerfiles` to define the deployment artifacts.

The backend artifact is a small-ish footprint java container which allows us to run the spring boot application with a complete jre/jdk installed.

The frontend artifact is a simple nginx server, which servers the frontend application, but with a little bit of extra logic. I added a proxy definition to encapsulate the backend and facade it behind this reverse proxy making frontend development easier in the process and deployment also. Not the `/api` route defers to our backend service, in development and in production/staging. For development I had to configure the proxy in the vite config to ensure the same behavior.

To deliver the artifacts I created github action which start a build process and run tests as far as they are provided. For this challenge I won't create any rules concerning coverage, static analysis and so on, keeping it simple.
After build the artifacts will be published in githubs container registry.

In addition I created a [docker-compose file](docker-compose.yaml) to start the application with a simple `docker compose up`
