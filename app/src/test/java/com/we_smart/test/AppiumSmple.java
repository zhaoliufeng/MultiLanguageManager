package com.we_smart.test;

/**
 * Created by zhaol on 2018/3/29.
 */
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Timestamp;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumSmple {

    private AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("platformVersion", "5.1");
        desiredCapabilities.setCapability("deviceName", "Meizu M3");
        desiredCapabilities.setCapability("appPackage", "com.ws.mesh.mesh_home");
        desiredCapabilities.setCapability("appActivity", "com.we_smart.doonne.view.activity.LauncherActivity");

        URL remoteUrl = new URL("http://localhost:4723/wd/hub");

        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
    }

    @Test
    public void sampleTest() {
        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.SECONDS);
        (new TouchAction(driver)).tap(417, 283).perform();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        (new TouchAction(driver)).tap(490, 507).perform();
//        (new TouchAction(driver)).tap(336, 594).perform();
//        (new TouchAction(driver)).tap(529, 566).perform();
//        (new TouchAction(driver)).tap(501, 698).perform();
//        (new TouchAction(driver)).tap(417, 1042).perform();
//        (new TouchAction(driver)).tap(375, 1174).perform();
    }

    @After
    public void tearDown() {
//        driver.quit();
    }
}
