# AI-Assisted Development Guide

AI-generated code is allowed only when it follows the same standards as human-written code.
Human review is mandatory.

## AI May Generate

- UI tests using existing page objects
- API tests using `RestClient`
- Contract test skeletons
- Page objects following the locator JSON pattern
- JSON test data that validates against schemas
- WireMock stubs and mock payloads
- Documentation updates

## AI Must Not Generate

- Hardcoded credentials, tokens, passwords, or API keys
- Global SSL bypasses
- Tests with `Thread.sleep`
- Tests with vague assertions such as only checking non-null values
- Shared mutable state across parallel tests
- Code that logs secrets
- Unreviewed changes to framework core, security helpers, or CI gates

## Required Prompt Context

Provide AI with:

- Test type: UI, API, contract, component, integration, or E2E
- Target service/page/module
- Requirement or story ID
- Expected assertions
- Test data source
- Cleanup expectations
- Existing page object or API client to reuse

## Review Checklist

- Code compiles.
- Test has meaningful assertions.
- Test belongs to the right TestNG group.
- Test data is externalized as JSON unless generated at runtime.
- API requests and UI failures attach evidence to Allure.
- No secrets are hardcoded or logged.
- No hard waits are used.
- Parallel execution is safe.
- Contract/schema validation is present where applicable.
