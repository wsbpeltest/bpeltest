package betsy.common.tasks;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class WaitTasks {
    /**
     * Sleeps/waits for a specific amount of milliseconds.
     *
     * @param milliseconds the duration to sleep/wait
     */
    public static void sleep(final int milliseconds) {
        if(milliseconds <= 0) {
            log.info("Did not sleep because value is " + milliseconds + "ms");
            return;
        }

        log.info("Sleep for " + String.valueOf(milliseconds) + " ms NOW");
        long max = System.currentTimeMillis() + milliseconds;
        while (max > System.currentTimeMillis()) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public static void waitFor(int untilMilliSeconds, int checkEveryMilliseconds, Callable<Boolean> c) {
        long max = System.currentTimeMillis() + untilMilliSeconds;

        try {
            while (max > System.currentTimeMillis()) {
                if (c.call()) {
                    return;
                }
                sleep(checkEveryMilliseconds);
            }
            if (!c.call()) {
                log.info("Condition not met of wait task within the specified time");
                throw new IllegalStateException("waited for " + untilMilliSeconds + "ms, but condition was not met");
            }

        } catch (IllegalStateException e) {
            throw e;// just rethrow
        } catch (Exception e) {
            throw new IllegalStateException("internal error", e);
        }
    }

    public static void waitForSubstringInFile(int untilMilliSeconds, int checkEveryMilliseconds, Path path, String substring) {
        waitFor(untilMilliSeconds, checkEveryMilliseconds, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return FileTasks.hasFileSpecificSubstring(path, substring);
            }
        });
    }

    public static void waitForAvailabilityOfUrl(int untilMilliSeconds, int checkEveryMilliseconds, URL url) {
        waitFor(untilMilliSeconds, checkEveryMilliseconds, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return URLTasks.isUrlAvailable(url);
            }
        });
    }

    public static void waitForAvailabilityOfUrl(int untilMilliSeconds, int checkEveryMilliseconds, String url) {
        try {
            waitForAvailabilityOfUrl(untilMilliSeconds, checkEveryMilliseconds, new URL(url));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("url to check is not a valid url", e);
        }
    }

    public static void waitForContentInUrl(int untilMilliSeconds, int checkEveryMilliseconds, URL url, String substring) {
        waitFor(untilMilliSeconds, checkEveryMilliseconds, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return URLTasks.hasUrlSubstring(url, substring);
            }
        });
    }

    private static final Logger log = Logger.getLogger(WaitTasks.class);
}