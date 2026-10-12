# CI/CD Guide

The `Jenkinsfile` supports:

- Parameterized environment selection
- Browser selection
- Suite selection
- Optional Selenium Grid startup
- AWS/Jenkins agent labels
- Allure publishing
- Artifact archival

Recommended stages:

1. Checkout
2. Quality gates
3. Contract tests
4. Component/API tests
5. Integration tests
6. E2E tests
7. Report publishing
8. Artifact archival
