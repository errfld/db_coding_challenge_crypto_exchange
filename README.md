# Deutsche Bank Coding Challenge

To interview for **_Senior Full Stack Developer (f/m/x) – Digital Asset Custody Technology_**, I created this repository. To give some idea and hints about my workflow you can find different states of the application isolated as branches.

You may find the following branches:

- [1_simple_solution](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/1_simple_solution) dirty and not refined, but does what it is meant to do. An application, or part of it, in this state may be allegible es proof of concepts or some brainstorming in code as a basis for team internal discussion.
- [2_ci-cd](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/2_ci-cd) simple CI/CD capabilities were added to allow going faster further down the road and was a new experience for me to setup github actions and using the github container registry.
- [3_refactor-1](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/3_refactor-1) started a refactoring process to encapsulate functionality and increase maintainability as well as ease of future improvements.
- [main](https://github.com/errfld/db_coding_challenge_crypto_exchange/tree/main) current development branch. Due to time constraints and pragmatism I am not sure how far I may improve on the code quality, because I assume you would just like to see if you applicant is able to get things done and improve from there and this is what I did.

### How to start

Prerequisites: java 17, node 18, docker (with compose), make

```sh
git clone https://github.com/errfld/db_coding_challenge_crypto_exchange.git
cd db_coding_challenge_crypto_exchange
make run
```

`make run` builds the backend, the frontend, creates docker images and starts these images with docker compose.

### Next possible steps

- Adding testing library for the frontend. I would go with something like `vitest` + `jsdom` + `react-testing-library`
- deployment to ECS or a similar service
- migrate development to [devcontainers](https://code.visualstudio.com/docs/devcontainers/containers)
- stablize UI behavior (jumps in columns)
- work on UX and perhaps remove currencys from the lower table after they are added to the watchlist
- refactor the API, structures like `key` -> `{ key, value }` are confusing at best
- showcasing websockets and the ability to update the UI whenever an exchange rate would change

### What is most urgent now?

Calling me, of course 😎
But in ernest, I hope you like my solution and I met your expectations. 

Best regards, 

Eran Riesenfeld
