# WebDriver binaries

This project uses [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) (and Selenium Manager as fallback) to download browser drivers at runtime.

Set `browser=edge` or `browser=chrome` in `resources/config.properties`.

Optional manual driver management:

- `msedgedriver.exe` for Microsoft Edge
- `chromedriver.exe` for Google Chrome

Place the matching executable in this folder. `WebDriverFactory` picks it up automatically for Edge; Chrome still uses WebDriverManager when no local driver is present.
