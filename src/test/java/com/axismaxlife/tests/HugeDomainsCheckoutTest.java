package com.axismaxlife.tests;

import com.axismaxlife.automation.config.ConfigReader;
import com.axismaxlife.automation.pages.demo.HugeDomainsCartPage;
import com.axismaxlife.automation.pages.demo.HugeDomainsCheckoutPage;
import com.axismaxlife.automation.pages.demo.HugeDomainsDomainPage;
import com.axismaxlife.automation.pages.demo.HugeDomainsHomePage;
import com.axismaxlife.automation.pages.demo.HugeDomainsSearchResultsPage;
import com.axismaxlife.automation.reporting.ReportManager;
import com.axismaxlife.automation.utils.DriverManager;
import com.axismaxlife.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HugeDomainsCheckoutTest extends BaseTest {

    @Test(description = "E2E: search a domain on HugeDomains, add it if listed, open cart, and stop at payment")
    public void shouldSearchAddToCartAndStopAtPayment() {
        String keyword = ConfigReader.get("demo.search.keyword");

        HugeDomainsHomePage homePage = new HugeDomainsHomePage(DriverManager.getDriver()).open();
        Assert.assertTrue(homePage.isLoaded(), "HugeDomains home page should load with a search box");
        ReportManager.logPass("Home page loaded");

        HugeDomainsSearchResultsPage resultsPage = homePage.search(keyword);
        Assert.assertTrue(resultsPage.isLoaded(), "Search results page should load for: " + keyword);
        Assert.assertTrue(resultsPage.showsAvailabilityStatus(),
                "Search results should show domain availability status for: " + keyword);
        ReportManager.logPass("Availability status for " + keyword + ": " + resultsPage.availabilityStatus());

        String status = resultsPage.availabilityStatus();
        if ("NO_RESULTS".equals(status) || "UNAVAILABLE".equals(status)) {
            String fallback = ConfigReader.getOptional("demo.search.fallback", "coffee");
            ReportManager.logStep("Exact domain is not listed for sale; searching listed keyword: " + fallback);
            resultsPage = homePage.search(fallback);
            Assert.assertTrue(resultsPage.isLoaded(), "Fallback search results should load for: " + fallback);
            Assert.assertTrue(resultsPage.showsAvailabilityStatus(),
                    "Fallback search should show availability for: " + fallback);
            ReportManager.logPass("Fallback availability status: " + resultsPage.availabilityStatus());
        }

        HugeDomainsCartPage cartPage;
        String domainToken = keyword;

        if (resultsPage.canAddToCart() && resultsPage.availabilityStatus().equals("FOR_SALE")) {
            cartPage = resultsPage.addToCart();
        } else {
            HugeDomainsDomainPage domainPage = resultsPage.openAvailableListing();
            Assert.assertTrue(domainPage.isDetailsPageDisplayed(),
                    "Domain details should load. Title: " + domainPage.pageTitle());
            Assert.assertNotNull(domainPage.availabilityStatus(), "Domain page should expose availability");
            ReportManager.logPass("Domain listing status: " + domainPage.availabilityStatus()
                    + " (" + domainPage.listedDomainName() + ")");

            Assert.assertTrue(domainPage.isAvailableForPurchase(),
                    "A listed/for-sale domain is required to continue to cart. Status: "
                            + domainPage.availabilityStatus());
            domainToken = domainPage.normalizedDomainToken();
            cartPage = domainPage.addToCart();
        }

        Assert.assertTrue(cartPage.isLoaded(), "Cart page should load after Add to Cart");
        Assert.assertTrue(cartPage.containsDomain(domainToken),
                "Cart should list the selected domain. Token: " + domainToken);
        ReportManager.logPass("Cart contains domain: " + domainToken);

        HugeDomainsCheckoutPage checkoutPage = cartPage.proceedToCheckout()
                .continueToPaymentWithoutEnteringCard();

        Assert.assertTrue(checkoutPage.isPaymentPageDisplayed(),
                "Checkout should reach a payment page. URL: "
                        + DriverManager.getDriver().getCurrentUrl());
        Assert.assertTrue(checkoutPage.paymentFieldsAreEmpty(),
                "Payment card fields must remain empty");
        Assert.assertTrue(checkoutPage.didNotSubmitPayment(),
                "Test must stop before payment submission");
        ReportManager.logPass("Stopped at payment page without entering card details");
    }
}
