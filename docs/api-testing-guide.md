# API Testing Guide

Use `RestClient` for common Rest Assured behavior:

- Base URI from `config/config.json`
- JSON content type
- Allure Rest Assured attachments
- Configurable timeout defaults
- Common HTTP methods

For each microservice, create a service-specific client that wraps endpoint paths and payloads.
Tests should call those clients rather than building raw requests inline.

Validate:

- Status codes
- Response body fields
- JSON schemas
- Contract compatibility where applicable
- Trace or correlation IDs for debugging
