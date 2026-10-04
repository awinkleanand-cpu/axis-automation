package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchPage;
import utils.BaseTest;

public class SearchPageTest extends BaseTest {

    @Test(description = "Test Case 2: Verify search functionality returns results")
    public void verifySearchReturnsResults() {
        SearchPage searchPage = new HomePage(getDriver())
                .openHomePage()
                .navigateToSearch()
                .enterDomainName("coffee.com")
                .clickSearch();

        Assert.assertTrue(searchPage.hasResults(),
                "Search should return domain listings or availability results for coffee.com");
    }

    @Test(description = "Test Case 3: Verify invalid domain search shows proper message")
    public void verifyInvalidDomainSearchShowsProperMessage() {
        SearchPage searchPage = new HomePage(getDriver())
                .openHomePage()
                .navigateToSearch()
                .enterDomainName("@@@not-a-valid-domain!!!")
                .clickSearch();

        Assert.assertTrue(searchPage.showsExpectedInvalidDomainMessage(),
                "Expected invalid-domain message was not shown. Expected: "
                        + SearchPage.EXPECTED_INVALID_DOMAIN_MESSAGE);
    }
}
