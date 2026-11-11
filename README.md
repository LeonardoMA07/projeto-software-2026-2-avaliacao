# API de Avaliações

API em Spring Boot (gerada pelo Spring Initializr) em que um usuário avalia outras pessoas da empresa.
Todas as rotas são autenticadas pelo Auth0.

| Rota | Quem pode |
| --- | --- |
| `POST /avaliacoes` | ADMIN |
| `GET /avaliacoes` | ADMIN e USER |

Entidade `Avaliacao`: `id`, `emailAvaliador`, `emailAvaliado`, `comentarios`, `nota` (0 a 10), `dataAvaliacao` (preenchida pelo back).

```json
POST /avaliacoes
{ "emailAvaliador": "admin@empresa.com", "emailAvaliado": "pessoa@empresa.com", "nota": 9, "comentarios": "Bom trabalho" }
```

## Auth0

- Issuer: `https://dev-cqcjca7w4bkovvlv.us.auth0.com/`, audience: `https://avaliacoes-api` (em `application.properties`).
- Os papéis vêm da claim `https://social-insper.com/roles`, adicionada por uma Action de Post-Login:

```js
exports.onExecutePostLogin = async (event, api) => {
  const namespace = 'https://social-insper.com';
  if (event.authorization) {
    api.accessToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
    api.idToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
  }
};
```

## Rodar localmente

```
docker run -d --name postgres-avaliacao -e POSTGRES_DB=avaliacaodb -e POSTGRES_USER=usuario -e POSTGRES_PASSWORD=senha -p 5432:5432 postgres:17-alpine
./mvnw spring-boot:run
```

## Deploy (GitHub Actions → EC2)

A cada push na `main`, o workflow `.github/workflows/deploy.yml` roda os testes, gera a imagem Docker,
copia para a EC2 e sobe os containers `postgres-avaliacao` e `avaliacao` (porta 8080) na rede `rede-avaliacao`.

Secrets do repositório: `HOST` (IP da EC2), `KEY` (chave .pem) e `DB_PASSWORD` (senha do Postgres).
