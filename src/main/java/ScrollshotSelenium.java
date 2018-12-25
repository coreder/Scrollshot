import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.Map;

import static java.lang.Math.toIntExact;

public class ScrollshotSelenium implements IScrollShot {
    private static final String GET_SCROLL_DIMS =
            "var elem = arguments[0];" +
                    "return {height: elem.scrollHeight, width: elem.scrollWidth};";
    private static final String GET_CLIENT_DIMS =
            "var elem = arguments[0];" +
                    "var rect = elem.parentElement.getBoundingClientRect();" +
                    "return {height: elem.parentElement.clientHeight, width: elem.parentElement.clientWidth, x: rect.x, y: rect.y };";
    private static final String SET_SCROLL_POS =
            "var elem = arguments[0];" +
                    "elem.parentElement.scrollTo(arguments[0],arguments[1]);" +
                    "return {top: elem.parentElement.scrollTop, left: elem.parentElement.scrollLeft};";
    private static final String GET_DEVICE_PIXEL_RATIO =
            "return window.devicePixelRatio;";
    private static final String SET_TRANSLATE_POS =
            "arguments[0].style.transform = \"translate(0px,\" + arguments[2] + \"px)\";" +
                    "var brect = arguments[0].getBoundingClientRect();" +
                    "return {top: brect.y, left: brect.x};";
    private long pixelRatio_;
    private WebDriver driver_;
    private int margin_ = 20; //Pixels

    public ScrollshotSelenium(WebDriver driver) throws InvalidClassException {
        if (!(driver instanceof TakesScreenshot)) //Unless bypassing webdriver in future
            throw new InvalidClassException("Driver isn't capable of taking screenshots");
        driver_ = driver;
    }

    public BufferedImage screenshot() {
        WebElement element = driver_.findElement(By.cssSelector("body"));
        return elementScreenshot(element);
    }

    public BufferedImage elementScreenshot(WebElement element) {
        try {
            return elementScreenshot_(element);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage elementScreenshot_(WebElement element) throws IOException {
        if (!(driver_ instanceof JavascriptExecutor)) {
            //Unable to take full page screenshot
            return capture(0, 0, 0, 0);
        }

        pixelRatio_ = (Long) jsFunc(GET_DEVICE_PIXEL_RATIO);

        Map<String, Long> scrollSize = jsFuncRetDims(GET_SCROLL_DIMS, element);
        long scrollWidth = scrollSize.get("width");
        long scrollHeight = scrollSize.get("height");
        BufferedImage bufferedImage = new BufferedImage(
                toIntExact(pixelRatio_ * scrollWidth),
                toIntExact(pixelRatio_ * scrollHeight),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();

        Map<String, Long> clientSize = jsFuncRetDims(GET_CLIENT_DIMS, element);
        int clientWidth = toIntExact(clientSize.get("width")); //TODO
        int clientHeight = toIntExact(clientSize.get("height"));
        int clientX = toIntExact(clientSize.get("x"));
        int clientY = toIntExact(clientSize.get("y"));

        long offsetTop = 0;
        int margin = 0;
        do {
            int fragH = Math.min(clientHeight - margin, toIntExact(scrollHeight + offsetTop));
            //Screenshot
            BufferedImage screenshot = capture(
                    toIntExact(pixelRatio_ * clientX),
                    toIntExact(pixelRatio_ * (clientY + margin)),
                    toIntExact(pixelRatio_ * clientWidth),
                    toIntExact(pixelRatio_ * fragH));
            //Join
            g2.drawImage(screenshot,
                    toIntExact(pixelRatio_ * 0 /*-offsetLeft*/),
                    toIntExact(pixelRatio_ * -offsetTop),
                    null);
            //Control
            if (offsetTop - fragH <= -scrollHeight)
                break;
            margin = margin_;
            //Progress
            Map<String, Long> scrollPos = jsFuncRetDims(SET_TRANSLATE_POS, element, 0, offsetTop - clientHeight + margin);
            offsetTop = scrollPos.get("top");
        } while (true);

        g2.dispose();
        return bufferedImage;
    }

    private Map<String, Long> jsFuncRetDims(String func, Object... params) {
        return (Map<String, Long>) jsFunc(func, params);
    }

    private Object jsFunc(String func, Object... params) {
        return ((JavascriptExecutor) driver_).executeScript(func, params);
    }

    private BufferedImage capture(int x, int y, int w, int h) throws IOException {
        byte[] bytes = ((TakesScreenshot) driver_).getScreenshotAs(OutputType.BYTES);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        if (w != 0 && h != 0)
            return img.getSubimage(x, y, w, h);
        return img;
    }
}
