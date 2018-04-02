package com.we_smart.test;

/**
 * Created by zhaol on 2018/3/29.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

public class AppiumSmple {

    private AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("platformVersion", "5.1");
        desiredCapabilities.setCapability("deviceName", "Meizu M3");
        desiredCapabilities.setCapability("appPackage", "com.we_smart.test");
        desiredCapabilities.setCapability("appActivity", ".StartActivity");

        URL remoteUrl = new URL("http://localhost:4723/wd/hub");

        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
    }

    @Test
    public void sampleTest() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement btn_turn = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"跳转\")");
        btn_turn.click();
        WebElement btn_en = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"英文\")");
        btn_en.click();
        WebElement btn_zh = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"简中\")");
        btn_zh.click();
        WebElement btn_tw = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"繁中\")");
        btn_tw.click();
    }

    @After
    public void tearDown() {
//        driver.quit();
    }
}
