package com.axismaxlife.tests;

import com.axismaxlife.automation.listeners.RetryAnalyzer;
import com.axismaxlife.automation.pages.ApplicationPage6;
import com.axismaxlife.automation.pages.DashboardPage;
import com.axismaxlife.automation.pages.LoginPage;
import com.axismaxlife.automation.reporting.ReportManager;
import com.axismaxlife.automation.utils.DriverManager;
import com.axismaxlife.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InsuranceApplicationTest extends BaseTest {

    @Test(
            description = "End-to-end insurance application workflow on Axis Max Life UAT portal",
            retryAnalyzer = RetryAnalyzer.class
    )
    public void shouldCompleteInsuranceApplicationWorkflow() {
        DashboardPage dashboardPage = new LoginPage(DriverManager.getDriver())
                .open()
                .login();

        Assert.assertNotNull(dashboardPage, "Dashboard page should load after login");
        ReportManager.logPass("Dashboard loaded successfully");

        ApplicationPage6 finalPage = dashboardPage
                .startNewApplication()
                .fillAndProceed()
                .fillAndProceed()
                .fillAndProceed()
                .fillAndProceed()
                .skipPosvAndProceed()
                .fillDetails();

        finalPage.submitApplication();

        boolean submitted = finalPage.isSubmissionSuccessful();
        ReportManager.logInfo("Submission verification result: " + submitted);
        Assert.assertTrue(submitted, "Application submission confirmation was not displayed");
        ReportManager.logPass("Insurance application submitted successfully");
    }
}
