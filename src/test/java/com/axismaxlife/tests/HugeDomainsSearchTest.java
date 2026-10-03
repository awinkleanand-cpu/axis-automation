package com.axismaxlife.tests;

import com.axismaxlife.automation.config.ConfigReader;
import com.axismaxlife.automation.pages.demo.HugeDomainsDomainPage;
import com.axismaxlife.automation.pages.demo.HugeDomainsHomePage;
import com.axismaxlife.automation.pages.demo.HugeDomainsSearchResultsPage;
import com.axismaxlife.automation.reporting.ReportManager;
import com.axismaxlife.automation.utils.DriverManager;
import com.axismaxlife.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HugeDomainsSearchTest extends BaseTest {

    @Test(description = "E2E: search a domain on HugeDomains and open the first listing")
    public void shouldSearchAndOpenDomainListing() {
        String keyword = ConfigReader.get("demo.search.keyword");

        HugeDomainsSearchResultsPage resultsPage = new HugeDomainsHomePage(DriverManager.getDriver())
                .open()
                .search(keyword);

        Assert.assertTrue(resultsPage.hasResults(), "Search results should be displayed for: " + keyword);
        ReportManager.logPass("Search results displayed for keyword: " + keyword);

        HugeDomainsDomainPage domainPage = resultsPage.openFirstResult();
        Assert.assertTrue(domainPage.isDetailsPageDisplayed(),
                "Domain details page should load. Title: " + domainPage.pageTitle());
        ReportManager.logPass("Domain details page opened: " + domainPage.pageTitle());
    }
}
