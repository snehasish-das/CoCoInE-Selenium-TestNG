# Framework Architecture

The framework separates UI, API, configuration, data, reporting, and lifecycle concerns.

- UI tests use Page Object Model and `SeleniumActions`.
- API tests use `RestClient` and service-specific clients.
- Contract tests should use Pact or Spring Cloud Contract.
- Component and integration tests can combine Rest Assured, WireMock, schemas, and service fixtures.
- E2E tests may combine UI and API setup, but assertions should remain user/business focused.

The framework is thread-safe by design. WebDriver is held in `ThreadLocal`, and test data should
be isolated per test.
