package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import utils.BaseTest;

public class HomePageTest extends BaseTest {

    @Test(description = "Test Case 1: Verify homepage loads successfully")
    public void verifyHomepageLoadsSuccessfully() {
        HomePage homePage = new HomePage(getDriver()).openHomePage();
        Assert.assertTrue(homePage.isTitleValid(),
                "Homepage should load with a HugeDomains title. Actual title: " + homePage.getPageTitle());
        Assert.assertTrue(getDriver().getCurrentUrl().toLowerCase().contains("hugedomains.com"),
                "Browser URL should remain on hugedomains.com");
    }
}
