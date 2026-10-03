package com.axismaxlife.tests;

import com.axismaxlife.automation.pages.demo.SauceDemoCartPage;
import com.axismaxlife.automation.pages.demo.SauceDemoInventoryPage;
import com.axismaxlife.automation.pages.demo.SauceDemoLoginPage;
import com.axismaxlife.automation.pages.demo.TheInternetHomePage;
import com.axismaxlife.automation.pages.demo.TheInternetSecurePage;
import com.axismaxlife.automation.reporting.ReportManager;
import com.axismaxlife.automation.utils.DriverManager;
import com.axismaxlife.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FailingScenariosTest extends BaseTest {

    @Test(description = "TC-F-01 Invalid The Internet password still reaches secure area")
    public void tcF01InvalidPasswordLogin() {
        TheInternetSecurePage secure = new TheInternetHomePage(DriverManager.getDriver())
                .openFormAuthentication()
                .login("tomsmith", "WrongPassword!");
        Assert.assertTrue(secure.isLoggedIn(), "Invalid password should not reach the secure area");
        ReportManager.logPass("Unexpectedly logged in");
    }

    @Test(description = "TC-F-02 Sauce Demo login with wrong password opens inventory")
    public void tcF02WrongSaucePassword() {
        SauceDemoInventoryPage inventory = new SauceDemoLoginPage(DriverManager.getDriver())
                .open()
                .login("standard_user", "bad_password");
        Assert.assertTrue(inventory.isLoaded(), "Wrong password should not open inventory");
    }

    @Test(description = "TC-F-03 Locked-out Sauce Demo user can browse products")
    public void tcF03LockedOutUser() {
        SauceDemoInventoryPage inventory = new SauceDemoLoginPage(DriverManager.getDriver())
                .open()
                .login("locked_out_user", "secret_sauce");
        Assert.assertTrue(inventory.isLoaded(), "Locked out user should not see inventory");
    }

    @Test(description = "TC-F-04 Empty cart contains a product")
    public void tcF04EmptyCartHasItems() {
        SauceDemoCartPage cart = new SauceDemoLoginPage(DriverManager.getDriver())
                .open()
                .login()
                .openCart();
        Assert.assertTrue(cart.hasItems(), "Cart was empty but test expected an item");
    }

    @Test(description = "TC-F-05 Sauce Demo title is HugeDomains")
    public void tcF05WrongTitleSauce() {
        new SauceDemoLoginPage(DriverManager.getDriver()).open();
        Assert.assertEquals(DriverManager.getDriver().getTitle(), "HugeDomains",
                "Sauce Demo title does not match HugeDomains");
    }

    @Test(description = "TC-F-06 The Internet home title is Swag Labs")
    public void tcF06WrongTitleInternet() {
        new TheInternetHomePage(DriverManager.getDriver()).open();
        Assert.assertEquals(DriverManager.getDriver().getTitle(), "Swag Labs",
                "The Internet title does not match Swag Labs");
    }

    @Test(description = "TC-F-07 Valid login lands on /admin")
    public void tcF07WrongSecureUrl() {
        new TheInternetHomePage(DriverManager.getDriver()).openFormAuthentication().login();
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("/admin"),
                "Valid login does not land on /admin");
    }

    @Test(description = "TC-F-08 Order is complete without adding a product")
    public void tcF08CheckoutWithoutPurchase() {
        WebDriver driver = DriverManager.getDriver();
        new SauceDemoLoginPage(driver).open().login();
        Assert.assertFalse(driver.findElements(By.className("complete-header")).isEmpty(),
                "Order confirmation is not shown without a purchase");
    }

    @Test(description = "TC-F-09 Backpack add button exists on the login page")
    public void tcF09BackpackOnLoginPage() {
        WebDriver driver = DriverManager.getDriver();
        new SauceDemoLoginPage(driver).open();
        Assert.assertFalse(driver.findElements(By.id("add-to-cart-sauce-labs-backpack")).isEmpty(),
                "Backpack button is not on the login page");
    }

    @Test(description = "TC-F-10 HugeDomains home page title is Swag Labs")
    public void tcF10HugeDomainsWrongTitle() {
        WebDriver driver = DriverManager.getDriver();
        driver.get("https://www.hugedomains.com/");
        Assert.assertEquals(driver.getTitle(), "Swag Labs",
                "HugeDomains title does not match Swag Labs");
    }
}
