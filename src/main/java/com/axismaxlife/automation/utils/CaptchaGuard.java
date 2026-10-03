package com.axismaxlife.automation.utils;

import org.openqa.selenium.WebDriver;

public final class CaptchaGuard {

    private CaptchaGuard() {
    }

    public static void assertNoBotChallenge(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl().toLowerCase();
        String pageTitle = driver.getTitle().toLowerCase();
        String pageSource = driver.getPageSource().toLowerCase();

        if (currentUrl.contains("perfdrive.com")
                || currentUrl.contains("validate.")
                || pageTitle.contains("captcha")
                || pageTitle.contains("radware")
                || pageSource.contains("bot manager")
                || pageSource.contains("perfdrive")) {
            throw new BotChallengeException(buildMessage(driver));
        }
    }

    private static String buildMessage(WebDriver driver) {
        return "UAT portal blocked automated access with Radware Bot Manager / captcha. "
                + "Current URL: " + driver.getCurrentUrl() + ". "
                + "Ask the Axis Max Life UAT team to whitelist this machine's IP for automation, "
                + "or run the test from an approved network. "
                + "See README section 'Radware / Bot Manager whitelist'.";
    }

    public static class BotChallengeException extends RuntimeException {
        public BotChallengeException(String message) {
            super(message);
        }
    }
}
