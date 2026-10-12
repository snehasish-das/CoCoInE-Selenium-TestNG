# Contract Testing Guide

Use Pact for consumer-driven contract testing unless the organization standardizes on Spring Cloud
Contract.

Recommended flow:

- Consumers publish contracts.
- Providers verify contracts in CI.
- Contract failures block merges.
- Contract artifacts are archived with Allure and CI build artifacts.

Contract tests should be tagged with the `contract` TestNG group.
