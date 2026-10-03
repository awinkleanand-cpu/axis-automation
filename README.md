# Axis Max Life Insurance Automation

Selenium WebDriver automation for the Axis Max Life insurance application workflow on the UAT portal.

## Tech Stack

- Java 17
- Selenium WebDriver 4.x
- TestNG
- WebDriverManager
- Page Object Model (POM)

## Project Structure

```
axis-automation/
├── drivers/                          # Optional manual WebDriver binaries
├── resources/
│   ├── config.properties             # Test data and environment config
│   └── documents/                    # Sample files for upload steps
├── src/main/java/com/axismaxlife/automation/
│   ├── base/                         # Base page abstraction
│   ├── config/                       # Config reader
│   ├── pages/                        # Page Object classes
│   └── utils/                        # WebDriver and wait helpers
└── src/test/java/com/axismaxlife/tests/
    └── InsuranceApplicationTest.java # End-to-end workflow test
```

## Prerequisites

- Java 17+
- Maven 3.9+
- Google Chrome browser

## Configuration

Update `resources/config.properties` if credentials or test data change.

## Run Tests

From the project root:

```bash
mvn clean test
```

Or run the TestNG suite directly:

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

## Workflow Covered

1. Login to `https://mprouat.axismaxlife.com`
2. Start a new application
3. Fill application pages 1 through 4
4. Skip POSV on page 5
5. Complete payment/details on page 6
6. Upload documents and submit

## Notes

- The framework uses `WebDriverWait` for dynamic elements.
- Locators are label-driven to work with common form patterns on insurance portals.
- If the UAT portal DOM changes, update the relevant page object locators.
