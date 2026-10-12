# AI-Ready Selenium 4 + Rest Assured Framework

This repository is a scalable Java automation framework for UI, API, contract, component,
integration, and E2E testing. It is designed for human and AI-assisted development with strict
conventions, review gates, and machine-readable metadata.

## Tech Stack

- Java 11 compatible source
- Maven
- TestNG
- Selenium 4
- Rest Assured
- Allure Reports
- Selenium Manager for native Selenium 4 driver resolution
- Jackson
- WireMock and Pact-ready test dependencies
- Jenkins, Docker Compose Selenium Grid, and AWS/Jenkins agent support

## Run Locally

```bash
mvn test
mvn test -Psanity
mvn test -Pprod -Denv=prod-like
mvn test -Dtest=SampleApiTest#getEndpointReturnsSuccess
mvn test -Dtest=GoRestApiReadTest
GOREST_TOKEN=your-token mvn test -Dtest=GoRestApiCrudTest
```

## Useful Parameters

```bash
mvn test \
  -Denv=qa \
  -Dbrowser=chrome \
  -DthreadCount=4 \
  -Dremote=false
```

GoREST write tests require `GOREST_TOKEN`. Without it, the write-flow tests are skipped with a clear reason.

For Grid:

```bash
docker compose -f grid/docker-compose.yml up -d
mvn test -Pgrid -Dremote=true -DgridUrl=http://localhost:4444/wd/hub
```

## Reports

Allure results are written to `target/allure-results`.

```bash
mvn allure:serve
```

## Test Groups

Supported groups:

- `sanity`
- `smoke`
- `regression`
- `BAT`
- `P1`
- `P2`
- `contract`
- `component`
- `integration`
- `e2e`
- `critical`

Production-like runs should use the `prod` profile. The production suite includes read-only
checks and policy-gated flows that stop before restricted steps.

## Project Layout

- `src/main/java/org/cocoine/framework`: reusable framework code
- `src/main/resources/config`: environment and credential placeholders
- `src/main/resources/locators`: JSON locators by module/page
- `src/test/java/org/cocoine/tests`: UI, API, contract, component, integration, and E2E tests
- `src/test/resources/testData`: JSON data-provider files
- `src/test/resources/suites`: TestNG suite files
- `docs`: framework and AI development guidance
- `grid`: Selenium Grid Docker Compose file

## Security

Do not hardcode credentials, tokens, API keys, or environment-specific secrets. Use Jenkins
credentials, environment variables, AWS Secrets Manager, or Vault.
