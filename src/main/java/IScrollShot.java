import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;

public interface IScrollShot {
    BufferedImage screenshot();

    BufferedImage elementScreenshot(WebElement locator);
}
