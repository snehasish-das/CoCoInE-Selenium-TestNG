# Test Writing Guide

## Rules

- Put tests in the correct package under `src/test/java/org/cocoine/tests`.
- Add TestNG groups for execution control.
- Use JSON data providers for static data.
- Use generated data only for unique runtime values.
- Keep assertions specific and business meaningful.
- Avoid hard waits; use Selenium waits or Awaitility.
- Keep cleanup explicit for created test data.

## Example API Command

```bash
mvn test -Dtest=SampleApiTest#getEndpointReturnsSuccess
```

## Example Suite Command

```bash
mvn test -Psanity -Denv=qa
```
