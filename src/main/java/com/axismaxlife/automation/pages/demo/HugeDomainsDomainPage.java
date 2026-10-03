package com.axismaxlife.automation.pages.demo;

import com.axismaxlife.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HugeDomainsDomainPage extends BasePage {

    public HugeDomainsDomainPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDetailsPageDisplayed() {
        String url = driver.getCurrentUrl().toLowerCase();
        String source = driver.getPageSource().toLowerCase();
        boolean buyOrPriceVisible = !driver.findElements(By.xpath(
                "//*[contains(translate(., 'BUYNOWPRICE', 'buynowprice'), 'buy')"
                        + " or contains(translate(., 'PRICE', 'price'), 'price')"
                        + " or contains(text(), '$')]")).isEmpty();

        return url.contains("domain")
                || source.contains("buy now")
                || source.contains("make an offer")
                || buyOrPriceVisible;
    }

    public String pageTitle() {
        return driver.getTitle();
    }
}
