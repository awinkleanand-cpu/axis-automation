package com.axismaxlife.automation.utils;

import org.openqa.selenium.WebDriver;

public final class CaptchaGuard {

    private CaptchaGuard() {
    }

    public static void assertNoBotChallenge(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl().toLowerCase();
        String pageTitle = driver.getTitle().toLowerCase();
        String pageSource = driver.getPageSource().toLowerCase();

        boolean blocked = currentUrl.contains("perfdrive.com")
                || pageTitle.contains("captcha")
                || pageTitle.contains("radware")
                || pageTitle.contains("just a moment")
                || pageTitle.contains("attention required")
                || pageSource.contains("bot manager")
                || pageSource.contains("perfdrive")
                || pageSource.contains("you have been blocked")
                || pageSource.contains("cf-browser-verification")
                || pageSource.contains("cdn-cgi/challenge");

        if (blocked) {
            throw new BotChallengeException(buildMessage(driver));
        }
    }

    private static String buildMessage(WebDriver driver) {
        return "The target site blocked automated access (captcha / bot protection). "
                + "Current URL: " + driver.getCurrentUrl() + ". "
                + "This framework does not bypass security challenges. "
                + "Use an approved test environment or request IP allowlisting.";
    }

    public static class BotChallengeException extends RuntimeException {
        public BotChallengeException(String message) {
            super(message);
        }
    }
}
