import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RegionsTests {

    private WebDriver driver;

    @Before
    public void setup () {
        //driver = new FirefoxDriver();
        driver = new ChromeDriver();
    }

    @Test
    public void test() throws IOException {
        driver.get("https://docs.docker.com/compose/gettingstarted/#step-1-setup");
        ScrollshotSelenium scrollshotSelenium = new ScrollshotSelenium(driver);
        WebElement element = driver.findElement(By.cssSelector("#navbar.nav-sidebar"));
        BufferedImage screenshot = scrollshotSelenium.elementScreenshot(element);
        boolean success = ImageIO.write(screenshot, "png",
                new File("Screenshots/element_screenshot.png"));
    }

    @After
    public void aftertest() {
        driver.close();
    }
}
