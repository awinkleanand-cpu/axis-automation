package com.axismaxlife.automation.listeners;

import com.axismaxlife.automation.config.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = ConfigReader.getOptionalInt("max.retry.count", 1);
        if (retryCount < maxRetry) {
            retryCount++;
            return true;
        }
        return false;
    }
}
