# Axis Max Life Insurance Automation Framework

Production-ready Selenium framework for automating the Axis Max Life insurance application workflow on the UAT portal.

## Framework Features

- **Page Object Model (POM)** for maintainable test design
- **WebDriverWait** for reliable synchronization with dynamic UI
- **ExtentReports** HTML dashboard with step-by-step logs and screenshots
- **JSON and Excel test results** for CI/CD integration
- **Email report on failure** with HTML report, Excel, and screenshot attachments
- **Log4j2** file and console logging
- **Screenshot capture** on every major step and on failure
- **TestNG listener** for automatic pass/fail reporting
- **Retry analyzer** for flaky UI recovery
- **Thread-safe DriverManager** for future parallel execution
- **GitHub Actions CI** with automated reports and artifact upload

## Project Structure

```
axis-automation/
├── drivers/                                    # Optional manual WebDriver binaries
├── resources/
│   ├── config.properties                       # Environment and test data
│   └── documents/                              # Upload test files
├── src/main/java/com/axismaxlife/automation/
│   ├── base/                                   # BasePage
│   ├── config/                                 # ConfigReader, FrameworkConstants
│   ├── listeners/                              # TestListener, RetryAnalyzer
│   ├── pages/                                  # LoginPage, ApplicationPage1-6, etc.
│   ├── reporting/                              # ExtentManager, ReportManager, TestResultWriter
│   └── utils/                                  # DriverManager, WaitUtils, ScreenshotUtils
├── src/test/java/com/axismaxlife/tests/
│   ├── base/BaseTest.java                      # Driver lifecycle
│   └── InsuranceApplicationTest.java           # E2E workflow test
└── test-output/                                # Generated after test run
    ├── reports/ExtentReport.html               # Main HTML report
    ├── results/test-results.json               # Machine-readable results
    ├── results/test-results.xlsx               # Excel summary report
    ├── screenshots/                            # Step and failure screenshots
    └── logs/automation.log                     # Execution logs
```

## Prerequisites

- Java 17+
- Maven 3.9+
- Google Chrome

## Run Tests

```bash
mvn clean test
```

## CI/CD (GitHub Actions)

Every push or pull request to `main` triggers the workflow in `.github/workflows/ci.yml`.

The pipeline:
1. Compiles the project
2. Installs Chrome and runs tests in headless mode
3. Publishes a pass/fail summary in the GitHub Actions run page
4. Uploads all reports as a downloadable artifact (retained for 14 days)

### View CI results

1. Open the repository on GitHub
2. Go to **Actions** → select the latest workflow run
3. Review the **Summary** tab for pass/fail counts
4. Download **automation-reports-*** artifact for:
   - `ExtentReport.html`
   - `test-results.xlsx`
   - `test-results.json`
   - Screenshots and logs

### Optional GitHub Secrets

Add these under **Settings → Secrets and variables → Actions**:

| Secret | Purpose |
|---|---|
| `INSURANCE_USER_ID` | Overrides `user.id` in CI |
| `INSURANCE_PASSWORD` | Overrides `password` in CI |
| `EMAIL_PASSWORD` | SMTP password for failure emails |

CI automatically runs Chrome in headless mode when the `CI` environment variable is set.

## View Results

After execution, open:

| Artifact | Path |
|---|---|
| HTML Report | `test-output/reports/ExtentReport.html` |
| JSON Results | `test-output/results/test-results.json` |
| Excel Results | `test-output/results/test-results.xlsx` |
| Logs | `test-output/logs/automation.log` |
| Screenshots | `test-output/screenshots/` |
| TestNG Report | `target/surefire-reports/index.html` |

## Workflow Covered

1. Login to `https://mprouat.axismaxlife.com`
2. Start new application
3. Fill pages 1–4 with configured test data
4. Skip POSV on page 5
5. Complete payment, documents, and terms on page 6
6. Submit and verify success message

## Configuration

Edit `resources/config.properties` for credentials, test data, waits, and retry count.

### Email on failure

1. Set `email.enabled=true` in `resources/config.properties`
2. Configure SMTP settings (`email.smtp.host`, `email.smtp.username`, etc.)
3. Set the password via environment variable (recommended):

```bash
set EMAIL_PASSWORD=your_app_password
mvn clean test
```

When any test fails, an email is sent with:
- Execution summary in the body
- `ExtentReport.html` attached
- `test-results.xlsx` attached
- Latest failure screenshot attached

## Notes

- Locators are label-driven for resilience across form layouts.
- If a step fails, check `ExtentReport.html` and the failure screenshot in `test-output/screenshots/`.
- Update page object locators if the UAT portal DOM changes.
