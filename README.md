# CoCoInE - Comprehensive Test Automation Framework

A robust, enterprise-grade Java-based test automation framework built on **Selenium WebDriver**, **TestNG**, and **REST-Assured**. CoCoInE supports UI testing, API testing, contract testing, and end-to-end test automation with advanced reporting and cloud integration capabilities.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Categories](#test-categories)
- [Advanced Features](#advanced-features)
- [CI/CD Integration](#cicd-integration)
- [Reporting](#reporting)

## Features

### Core Capabilities
- ✅ **Data-Driven Testing** - JSON-based test data configuration
- ✅ **Parallel Execution** - Run tests and suites in parallel for faster execution
- ✅ **Automatic Screenshots** - Capture screenshots on test failure
- ✅ **Custom Logging** - Detailed test execution logs with custom formatters
- ✅ **Maven Integration** - Run tests via Maven commands
- ✅ **Advanced Reporting** - Extent Reports with hierarchical test steps and nodes

### UI Testing
- ✅ Cross-browser automation (Chrome, Firefox, Safari, Edge)
- ✅ Form submission and validation
- ✅ File upload and download handling
- ✅ Dynamic element selection and waits
- ✅ Drag and Drop operations
- ✅ Frame and window switching
- ✅ Alert handling and interaction
- ✅ Cross-browser testing via LambdaTest cloud

### API Testing
- ✅ RESTful service testing (GET, POST, PUT, PATCH, DELETE)
- ✅ Authentication methods (OAUTH2, Bearer Token, Basic Auth)
- ✅ File upload and download via API
- ✅ JSON path assertions for complex response validation
- ✅ Proxy configuration support
- ✅ Custom timeout and retry configuration

### DevOps & Cloud Integration
- ✅ Docker containerization
- ✅ AWS execution support
- ✅ Cross-browser testing via LambdaTest
- ✅ CI/CD pipeline ready

## Technology Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| **Selenium** | 4.9.1 | UI test automation |
| **TestNG** | 7.4.0 | Test framework and reporting |
| **REST-Assured** | 5.3.0 | API testing |
| **WebDriverManager** | 5.3.3 | Automatic driver management |
| **Extent Reports** | 5.0.9 | Advanced test reporting |
| **SLF4J** | 2.0.7 | Logging framework |
| **Java** | 8+ | Programming language |
| **Maven** | 3.6+ | Build tool |

## Project Structure

```
CoCoInE-Selenium-TestNG/
├── src/
│   ├── main/java/org/cocoine/
│   │   ├── config/          # Configuration files (credentials, settings)
│   │   ├── core/            # Core framework classes (CoCoIne.java)
│   │   └── modules/         # Test module base classes
│   │       ├── ComponentTest.java
│   │       ├── ContractTest.java
│   │       ├── EndToEndTest.java
│   │       └── IntegrationTest.java
│   │
│   └── test/java/org/cocoine/
│       ├── component/       # Component-level tests
│       ├── contract/        # Contract-based tests
│       ├── end/to/end/      # End-to-end tests
│       └── integration/     # Integration tests
│
├── pom.xml                  # Maven configuration
├── testframework.iml        # IntelliJ project file
└── README.md               # This file
```

## Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven 3.6 or higher
- Git

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd CoCoInE-Selenium-TestNG
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure credentials** (if needed)
   - Update `src/main/java/org/cocoine/config/creds.json` with your test credentials

## Configuration

### Credentials Configuration
Store your test credentials in `src/main/java/org/cocoine/config/creds.json`:

```json
{
  "username": "your_username",
  "password": "your_password",
  "api_key": "your_api_key"
}
```

### WebDriverManager
WebDriverManager handles automatic browser driver management. Supported browsers:
- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Apple Safari

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Category
```bash
# Component tests
mvn test -Dtest=component.**

# Contract tests
mvn test -Dtest=contract.**

# End-to-End tests
mvn test -Dtest=end.to.end.**

# Integration tests
mvn test -Dtest=integration.**
```

### Run Tests in Parallel
```bash
mvn test -DparallelRun=true
```

### Run with Specific Browser
```bash
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
```

## Test Categories

### Component Testing
Unit-level testing of individual components with focused scope.
- **Location:** `src/test/java/org/cocoine/component/`
- **Base Class:** `ComponentTest.java`

### Contract Testing
Service contract validation and API contract testing.
- **Location:** `src/test/java/org/cocoine/contract/`
- **Base Class:** `ContractTest.java`

### End-to-End Testing
Complete workflow testing across multiple systems.
- **Location:** `src/test/java/org/cocoine/end/to/end/`
- **Base Class:** `EndToEndTest.java`

### Integration Testing
Multi-component integration and workflow testing.
- **Location:** `src/test/java/org/cocoine/integration/`
- **Base Class:** `IntegrationTest.java`

## Advanced Features

### UI Test Scenarios
1. **Form Handling** - Launch URL and form submission
2. **File Operations** - Upload and download files
3. **Element Selection** - Dynamic element selection with waits
4. **User Interactions** - Drag and drop, double-click, hover
5. **Window Management** - Switch between frames and windows
6. **Alert Handling** - Accept/dismiss/interact with JavaScript alerts

### REST API Test Scenarios
1. **HTTP Methods** - GET, POST, PUT, PATCH, DELETE operations
2. **Authentication** - OAUTH2, Bearer Token, Basic Auth
3. **File Transfer** - Upload/download files via REST
4. **Response Validation** - JSONPath assertions for complex structures
5. **Advanced Configuration** - Proxy settings, custom timeouts, retry logic

## CI/CD Integration

### Docker Support
Build Docker images for containerized test execution:
```bash
docker build -t cocoine-tests .
docker run cocoine-tests mvn test
```

### AWS Execution
Execute tests on AWS infrastructure for scalable test runs.

### LambdaTest Integration
Run cross-browser tests on LambdaTest cloud infrastructure for comprehensive browser coverage.

## Reporting

### Extent Reports
All test runs generate comprehensive HTML reports with:
- Test execution timeline
- Hierarchical test steps and nodes
- Screenshots of failures
- Custom logging output
- Pass/fail statistics

**Report Location:** Generated in the project's `test-output` or `extent-reports` directory (configurable)

### Screenshot on Failure
Automatic screenshots are captured for all failed UI tests, embedded in the Extent Report.

### Custom Logging
All tests include detailed custom logging for debugging and analysis.

## Best Practices

1. **Use Page Object Model** - Organize UI elements and actions in page classes
2. **Data-Driven Tests** - Utilize JSON configuration for test data
3. **Explicit Waits** - Always use explicit waits instead of sleep()
4. **Descriptive Assertions** - Use meaningful assertion messages
5. **Test Independence** - Ensure tests can run in any order
6. **Parallel Execution** - Leverage parallel execution for faster feedback

## Troubleshooting

### WebDriver Issues
- Ensure WebDriverManager is properly configured
- Check Java PATH environment variable
- Verify browser compatibility with Selenium version

### Test Failures
- Check Extent Reports for detailed failure information
- Review automatic screenshots in the reports
- Check custom logs for step-by-step execution details

### Maven Build Issues
```bash
mvn clean install -U  # Force update dependencies
mvn dependency:resolve  # Resolve dependency conflicts
```

## Contributing

Follow the existing test structure and module organization when adding new tests.

## License

[Add your license information here]

## Support & Documentation

For issues and questions, please refer to:
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [REST-Assured Documentation](https://rest-assured.io/)
- [Extent Reports Documentation](https://www.extentreports.com/)