package com.axismaxlife.tests;

import com.axismaxlife.automation.pages.demo.SauceDemoCheckoutPage;
import com.axismaxlife.automation.pages.demo.SauceDemoInventoryPage;
import com.axismaxlife.automation.pages.demo.SauceDemoLoginPage;
import com.axismaxlife.automation.reporting.ReportManager;
import com.axismaxlife.automation.utils.DriverManager;
import com.axismaxlife.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SauceDemoCheckoutTest extends BaseTest {

    @Test(description = "E2E: login, add backpack to cart, checkout and complete order on Sauce Demo")
    public void shouldCompleteSauceDemoCheckout() {
        SauceDemoInventoryPage inventoryPage = new SauceDemoLoginPage(DriverManager.getDriver())
                .open()
                .login();

        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory should load after login");
        ReportManager.logPass("Inventory page loaded");

        SauceDemoCheckoutPage checkoutPage = inventoryPage
                .addBackpackAndOpenCart()
                .checkout()
                .fillCustomerDetails()
                .finishOrder();

        Assert.assertTrue(checkoutPage.isOrderComplete(), "Order confirmation should be displayed");
        ReportManager.logPass("Sauce Demo checkout completed successfully");
    }
}
