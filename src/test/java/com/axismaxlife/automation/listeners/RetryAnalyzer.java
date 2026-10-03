package com.axismaxlife.automation.listeners;

import com.axismaxlife.automation.config.ConfigReader;
import com.axismaxlife.automation.utils.CaptchaGuard;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (isCiEnvironment() || isBotChallengeFailure(result)) {
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

    private boolean isBotChallengeFailure(ITestResult result) {
        Throwable throwable = result.getThrowable();
        while (throwable != null) {
            if (throwable instanceof CaptchaGuard.BotChallengeException) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }
}
