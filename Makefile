all: be fe

run: all compose-up

be:
	cd ./backend && \
	./mvnw package -DskipTests && \
	docker build . --tag ghcr.io/errfld/db_coding_challenge_crypto_exchange_backend:latest

fe:
	cd frontend && \
	yarn install && \
	yarn build && \
	docker build . --tag ghcr.io/errfld/db_coding_challenge_crypto_exchange_frontend:latest

compose-up:
	docker-compose up