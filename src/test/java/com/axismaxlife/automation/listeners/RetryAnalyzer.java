package com.axismaxlife.automation.listeners;

import com.axismaxlife.automation.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (isCiEnvironment()) {
            return false;
        }

        int maxRetry = ConfigReader.getOptionalInt("max.retry.count", 1);
        if (retryCount < maxRetry) {
            retryCount++;
            return true;
        }
        return false;
    }

    private boolean isCiEnvironment() {
        String ci = System.getenv("CI");
        return ci != null && Boolean.parseBoolean(ci);
    }
}
