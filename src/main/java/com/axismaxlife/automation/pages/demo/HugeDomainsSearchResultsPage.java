package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import com.axismaxlife.automation.utils.CaptchaGuard;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HugeDomainsSearchResultsPage extends BasePage {

    private static final By RESULT_LINKS = By.xpath(
            "//a[contains(@href, 'domain_profile') or contains(@href, '.com') or contains(@href, 'domain')]"
                    + "[not(contains(@href, 'javascript'))]");

    public HugeDomainsSearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasResults() {
        return !driver.findElements(RESULT_LINKS).isEmpty()
                || driver.getPageSource().toLowerCase().contains("domain");
    }

    public HugeDomainsDomainPage openFirstResult() {
        CaptchaGuard.assertNoBotChallenge(driver);
        List<WebElement> links = driver.findElements(RESULT_LINKS);
        WebElement firstVisible = links.stream()
                .filter(WebElement::isDisplayed)
                .filter(link -> {
                    String text = link.getText();
                    String href = link.getAttribute("href");
                    return (text != null && text.contains("."))
                            || (href != null && href.contains("domain_profile"));
                })
                .findFirst()
                .orElseGet(() -> links.stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "No search result links found. URL: " + driver.getCurrentUrl())));

        logStep("Open first domain result: " + firstVisible.getText());
        firstVisible.click();
        waitUtils.waitForPageLoad();
        CaptchaGuard.assertNoBotChallenge(driver);
        logStepWithScreenshot("Domain details page loaded");
        return new HugeDomainsDomainPage(driver);
    }
}
