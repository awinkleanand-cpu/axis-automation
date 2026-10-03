package com.axismaxlife.tests;

import com.axismaxlife.automation.pages.demo.SauceDemoCartPage;
import com.axismaxlife.automation.pages.demo.SauceDemoCheckoutPage;
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

public class PassingScenariosTest extends BaseTest {

    @Test(description = "TC-P-01 Open The Internet home page")
    public void tcP01HomePageLoads() {
        TheInternetHomePage home = new TheInternetHomePage(DriverManager.getDriver()).open();
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("the-internet.herokuapp.com"),
                "Home URL should be The Internet");
        Assert.assertTrue(home != null);
        ReportManager.logPass("The Internet home page loaded");
    }

    @Test(description = "TC-P-02 Form Authentication login fields are visible")
    public void tcP02LoginFormVisible() {
        new TheInternetHomePage(DriverManager.getDriver()).openFormAuthentication();
        Assert.assertFalse(DriverManager.getDriver().findElements(By.id("username")).isEmpty(),
                "Username field should be visible");
        ReportManager.logPass("Login form displayed");
    }

    @Test(description = "TC-P-03 Valid The Internet login reaches secure area")
    public void tcP03ValidLogin() {
        TheInternetSecurePage secure = new TheInternetHomePage(DriverManager.getDriver())
                .openFormAuthentication()
                .login();
        Assert.assertTrue(secure.isLoggedIn(), "Valid credentials should reach the secure area");
        ReportManager.logPass("Logged in to secure area");
    }

    @Test(description = "TC-P-04 Logout returns to the login page")
    public void tcP04Logout() {
        TheInternetSecurePage secure = new TheInternetHomePage(DriverManager.getDriver())
                .openFormAuthentication()
                .login();
        secure.logout();
        Assert.assertTrue(secure.isLoggedOut(), "Logout should return to the login page");
        ReportManager.logPass("Logged out successfully");
    }

    @Test(description = "TC-P-05 Sauce Demo login page loads")
    public void tcP05SauceLoginPage() {
        new SauceDemoLoginPage(DriverManager.getDriver()).open();
        Assert.assertTrue(DriverManager.getDriver().getTitle().contains("Swag Labs"),
                "Sauce Demo title should contain Swag Labs");
        ReportManager.logPass("Sauce Demo login page loaded");
    }

    @Test(description = "TC-P-06 Valid Sauce Demo login opens inventory")
    public void tcP06SauceInventory() {
        SauceDemoInventoryPage inventory = new SauceDemoLoginPage(DriverManager.getDriver())
                .open()
                .login();
        Assert.assertTrue(inventory.isLoaded(), "Inventory should load after valid login");
        ReportManager.logPass("Inventory loaded");
    }

    @Test(description = "TC-P-07 Add backpack and verify cart")
    public void tcP07AddToCart() {
        SauceDemoCartPage cart = new SauceDemoLoginPage(DriverManager.getDriver())
                .open()
                .login()
                .addBackpackAndOpenCart();
        Assert.assertTrue(cart.hasItems(), "Cart should contain the backpack");
        ReportManager.logPass("Backpack listed in cart");
    }

    @Test(description = "TC-P-08 Complete Sauce Demo checkout")
    public void tcP08CheckoutComplete() {
        SauceDemoCheckoutPage checkout = new SauceDemoLoginPage(DriverManager.getDriver())
                .open()
                .login()
                .addBackpackAndOpenCart()
                .checkout()
                .fillCustomerDetails()
                .finishOrder();
        Assert.assertTrue(checkout.isOrderComplete(), "Order confirmation should be shown");
        ReportManager.logPass("Checkout completed");
    }

    @Test(description = "TC-P-09 Secure area URL contains /secure")
    public void tcP09SecureUrl() {
        new TheInternetHomePage(DriverManager.getDriver()).openFormAuthentication().login();
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("/secure"),
                "URL should contain /secure after login");
        ReportManager.logPass("Secure URL verified");
    }

    @Test(description = "TC-P-10 Sauce Demo login button is present")
    public void tcP10LoginButtonPresent() {
        WebDriver driver = DriverManager.getDriver();
        new SauceDemoLoginPage(driver).open();
        Assert.assertFalse(driver.findElements(By.id("login-button")).isEmpty(),
                "Login button should be present");
        ReportManager.logPass("Login button present");
    }
}
