# CoCoInE Selenium TestNG

A simple, generic Java automation framework using Selenium 4, TestNG, and Rest Assured.

The framework is intentionally small and readable. Page objects and REST wrappers contain actions;
tests contain assertions.

## Tech Stack

- Java 11
- Maven
- Selenium 4
- TestNG
- Rest Assured
- Jackson
- SLF4J Simple

## Project Layout

- `src/main/java/org/cocoine/core` - base UI/API helpers, config loading, and listeners
- `src/main/java/org/cocoine/pages/ui` - Selenium page objects
- `src/main/java/org/cocoine/pages/rest` - REST wrapper classes
- `src/main/resources/config` - framework configuration
- `src/main/resources/locators` - JSON locator files
- `src/test/java/org/cocoine/tests` - TestNG tests
- `src/test/resources/testData` - test data files
- `testng.xml` and `sanity-suite.xml` - TestNG suites

## Run Tests

```bash
mvn test
mvn test -Psanity
mvn test -Dtest=SampleApiTest
mvn test -Dtest=SampleUiTest#homePageLoads
```

## Common Parameters

```bash
mvn test \
  -DbaseUrl=https://example.org \
  -DapiBaseUrl=https://httpbin.org \
  -Dbrowser=chrome \
  -Dheadless=true
```

For Selenium Grid:

```bash
mvn test -Pgrid -Dremote=true -DgridUrl=http://localhost:4444/wd/hub
```

## Configuration

`src/main/resources/config/config.json` uses generic keys:

- `Environment.baseUrl`
- `Environment.apiBaseUrl`
- `Environment.browser`
- `Environment.remote`
- `Environment.gridUrl`
- `Environment.timeoutSeconds`
- `Applications.<appName>`
- `Users.<userRole>`

System properties override config values where supported.

## Writing Tests

UI tests should extend `BaseClass`, create page objects with `getDriver()`, perform actions through
page methods, and keep assertions inside the test.

API tests should call wrapper classes under `org.cocoine.pages.rest` and assert response status/body
inside the test.
