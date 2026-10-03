package com.axismaxlife.tests;

import com.axismaxlife.automation.pages.demo.TheInternetHomePage;
import com.axismaxlife.automation.pages.demo.TheInternetSecurePage;
import com.axismaxlife.automation.reporting.ReportManager;
import com.axismaxlife.automation.utils.DriverManager;
import com.axismaxlife.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TheInternetLoginTest extends BaseTest {

    @Test(description = "E2E: Form Authentication login and logout on the-internet.herokuapp.com")
    public void shouldLoginAndLogoutOnTheInternet() {
        TheInternetSecurePage securePage = new TheInternetHomePage(DriverManager.getDriver())
                .openFormAuthentication()
                .login();

        Assert.assertTrue(securePage.isLoggedIn(), "Should land on secure area. Flash: " + securePage.flashMessage());
        ReportManager.logPass("Logged in successfully");

        securePage.logout();
        Assert.assertTrue(securePage.isLoggedOut(), "Should return to login after logout. Flash: " + securePage.flashMessage());
        ReportManager.logPass("Logged out successfully");
    }
}
