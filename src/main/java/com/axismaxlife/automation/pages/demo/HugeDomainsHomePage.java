package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HugeDomainsHomePage extends HugeDomainsBasePage {

    private static final List<By> SEARCH_BOX_LOCATORS = List.of(
            By.cssSelector("input[name='query']"),
            By.cssSelector("input[name='domain_name']"),
            By.cssSelector("input[name='domain']"),
            By.cssSelector("input[type='search']"),
            By.cssSelector("input[placeholder*='Search' i]"),
            By.cssSelector("input[placeholder*='domain' i]"),
            By.cssSelector("input[type='text']:not([type='hidden'])")
    );

    private static final List<By> SEARCH_BUTTON_LOCATORS = List.of(
            By.xpath("//button[contains(translate(normalize-space(.), 'SEARCH', 'search'), 'search')]"),
            By.cssSelector("button[type='submit']"),
            By.cssSelector("input[type='submit']")
    );

    public HugeDomainsHomePage(WebDriver driver) {
        super(driver);
    }

    public HugeDomainsHomePage open() {
        logStep("Open HugeDomains home: " + ConfigReader.get("demo.url"));
        driver.get(ConfigReader.get("demo.url"));
        waitUtils.waitForPageLoad();
        dismissOverlays();
        waitUtils.waitForFirstVisible(SEARCH_BOX_LOCATORS);
        guardAndCapture("Home page loaded - title: " + driver.getTitle());
        return this;
    }

    public boolean isLoaded() {
        String url = currentUrl();
        return url.contains("hugedomains.com") && isAnyDisplayed(SEARCH_BOX_LOCATORS);
    }

    public HugeDomainsSearchResultsPage search(String keyword) {
        logStep("Search for domain: " + keyword);
        WebElement searchBox = waitUtils.waitForFirstVisible(SEARCH_BOX_LOCATORS);
        searchBox.clear();
        searchBox.sendKeys(keyword);

        boolean clickedSearch = false;
        if (isAnyDisplayed(SEARCH_BUTTON_LOCATORS)) {
            clickFirstVisible(SEARCH_BUTTON_LOCATORS, "Search button");
            clickedSearch = true;
        }
        if (!clickedSearch) {
            searchBox.sendKeys(Keys.ENTER);
        }

        HugeDomainsSearchResultsPage resultsPage = new HugeDomainsSearchResultsPage(driver);
        resultsPage.waitUntilLoaded();
        guardAndCapture("Search results loaded for: " + keyword);
        return resultsPage;
    }
}
