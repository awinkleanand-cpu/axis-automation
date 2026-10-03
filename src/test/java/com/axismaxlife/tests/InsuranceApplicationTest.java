package com.axismaxlife.tests;

import com.axismaxlife.automation.pages.ApplicationPage6;
import com.axismaxlife.automation.pages.DashboardPage;
import com.axismaxlife.automation.pages.LoginPage;
import com.axismaxlife.automation.utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InsuranceApplicationTest {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = WebDriverFactory.createDriver();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "End-to-end insurance application workflow on Axis Max Life UAT portal")
    public void shouldCompleteInsuranceApplicationWorkflow() {
        DashboardPage dashboardPage = new LoginPage(driver)
                .open()
                .login();

        ApplicationPage6 finalPage = dashboardPage
                .startNewApplication()
                .fillAndProceed()
                .fillAndProceed()
                .fillAndProceed()
                .fillAndProceed()
                .skipPosvAndProceed()
                .fillDetails();

        finalPage.submitApplication();
    }
}
