# Troubleshooting

## Browser Does Not Start

- Confirm browser is installed.
- Confirm Selenium Manager can resolve/download the browser driver.
- For CI, prefer Selenium Grid.

## Grid Fails

```bash
docker compose -f grid/docker-compose.yml ps
docker compose -f grid/docker-compose.yml logs
```

## Allure Report Missing Attachments

- Confirm `target/allure-results` exists.
- Confirm listeners are present in the TestNG suite.
- Confirm failures happen after WebDriver initialization for UI screenshots.

## API Tests Fail Locally

- Check `-Denv`.
- Check `restBaseUrl` in `config/config.json`.
- Check credentials are supplied through environment variables or Jenkins credentials.
