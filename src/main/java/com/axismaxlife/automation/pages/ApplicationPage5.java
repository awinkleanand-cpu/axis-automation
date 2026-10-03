package com.axismaxlife.automation.pages;

import com.axismaxlife.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ApplicationPage5 extends BasePage {

    private static final By SKIP_POSV = By.xpath(
            "//button[contains(normalize-space(.), 'Skip')]"
                    + " | //a[contains(normalize-space(.), 'Skip')]"
                    + " | //*[contains(normalize-space(.), 'Skip POSV')]"
                    + " | //*[contains(normalize-space(.), 'Skip') and (self::button or self::a)]");

    public ApplicationPage5(WebDriver driver) {
        super(driver);
    }

    public ApplicationPage6 skipPosvAndProceed() {
        if (!driver.findElements(SKIP_POSV).isEmpty()) {
            waitUtils.click(SKIP_POSV);
            waitUtils.waitForPageLoad();
        } else {
            waitUtils.clickButtonByText("Skip");
        }
        return new ApplicationPage6(driver);
    }
}
