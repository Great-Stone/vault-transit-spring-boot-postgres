# Vault Transit - Spring Boot with Postgresql

## Run Postgresql - Docker

```bash
$ cd terraform-postgresql-docker
$ terraform apply
```

## Set Postgresql

```bash
psql -h 127.0.0.1 -p 5432 -d mydb -U admin -W
```

```sql
CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL
);
```

## Set Vault Transit

```bash
vault server -dev -dev-root-token-id=root -log-level=trace
```

```bash
export VAULT_ADDR=http://127.0.0.1:8200;
export VAULT_TOKEN=root
```

```bash
vault secrets enable transit
```

```bash
vault write -f transit/keys/my-key
```

```bash
$ vault write transit/encrypt/my-key plaintext=$(echo "my secret data" | base64)

Key           Value
---           -----
ciphertext    vault:v1:8SDd3WHDOjf7mq69CyCqYjBXAiQQAVZRkFM13ok481zoCmHnSeDX9vyf7w==
```

## Test

```bash
curl -X GET http://localhost:8080/insert?content=hello
curl -X GET http://localhost:8080/insert?content=hello
curl -X GET http://localhost:8080/insert?content=hello
curl -X GET http://localhost:8080/insert?content=hello
```

```bash
curl -X GET http://localhost:8080/select | jq .
```

```bash
$ curl -X GET http://localhost:8080/select-encrypted | jq .
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   253    0   253    0     0   3708      0 --:--:-- --:--:-- --:--:--  3892
[
  {
    "id": 1,
    "content": "hello"
  },
  {
    "id": 2,
    "content": "vault:v1:XsHtl4wBndE7TnVjAF2BPciuMzanxnMOZtogJxzhULY1"
  },
  {
    "id": 3,
    "content": "vault:v1:gicecoJjAtyKoD0AMHXciQn1EYYviQavIlKoQvXtc4/Y"
  },
  {
    "id": 4,
    "content": "vault:v1:YKVW5ZRjxML3CY7RcJzFYaftqCCxGOhLMK8dBFhUuu2d"
  }
]
```

```bash
$ curl -X GET http://localhost:8080/select-decrypt | jq .
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100   109    0   109    0     0   1435      0 --:--:-- --:--:-- --:--:--  1535
[
  {
    "id": 1,
    "content": "hello"
  },
  {
    "id": 2,
    "content": "hello"
  },
  {
    "id": 3,
    "content": "hello"
  },
  {
    "id": 4,
    "content": "hello"
  }
]
```