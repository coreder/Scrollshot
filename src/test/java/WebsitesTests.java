import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class WebsitesTests {
    private WebDriver driver;

    @Parameter(0)
    public String url;

    @Parameter(1)
    public String imgName;

    @Parameters
    public static Collection<String[]> getUrls() {
        return Arrays.asList(new String[][]{
                {"https://applitools.com", "Applitools"},
                {"https://saucelabs.com", "Saucelabs"},
                {"http://webdriver.io/", "WebdriverIO"},
                {"http://nightwatchjs.org/", "NightwatchJS"},
                {"http://appium.io/", "Appium"},
                {"https://www.browserstack.com/", "Browserstack"},
                {"https://www.perfecto.io/", "PerfectoMobile"}
        });
    }

    @Before
    public void setup() {
        //driver = new FirefoxDriver();
        driver = new ChromeDriver();
    }

    @Test
    public void test() throws IOException {
        driver.get(url);
        ScrollshotSelenium scrollshotSelenium = new ScrollshotSelenium(driver);
        BufferedImage screenshot = scrollshotSelenium.screenshot();
        ImageIO.write(screenshot, "png",
                new File("Screenshots/fullpage_" + imgName + ".png"));
    }

    @After
    public void aftertest() {
        driver.close();
    }
}
