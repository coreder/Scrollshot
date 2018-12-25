import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;

public interface IScrollshot {
    BufferedImage screenshot();

    BufferedImage elementScreenshot(WebElement locator);
}
