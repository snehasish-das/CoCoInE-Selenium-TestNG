# Quality, Security, And Governance

Quality gates:

- Compilation
- TestNG execution
- Checkstyle
- SpotBugs
- Dependency vulnerability scanning
- Secret scanning in CI
- Contract verification
- JSON schema validation

Governance:

- CODEOWNERS protects framework core and sensitive areas.
- AI-generated code requires human review.
- Security-sensitive changes require security owner review.
- Flaky tests should be tagged and quarantined rather than hidden with broad retries.
