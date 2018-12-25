# Scrollshot
Screenshot through scrolling, generate unified screenshots in selenium.

#### General
Once trying to take a screenshot in Selenium you will find out that only the visible section of a presented web page will be captured.  
The Scrollshot project provides complimentary logic to capture web pages or scrollable elements through
all their scrollable sections and unifies the result into a single image.

Note that this is an initial version of the Scrollshot, as a result the interface might suffer
from changes. (aka. nothing is written in stone, yet)

#### Prerequisites
* Java 8 or higher (JDK > 1.8)
* Selenium and browsers specific drivers

#### Example
Using Scrollshot is pretty straight forward, just create ScrollshotSelenium object
and use it to take screenshots every time you need it.

```java
//Create ScrollshotSelenium object
IScrollshot scrollshot = new ScrollshotSelenium(driver);
//Create screenshot by calling .screenshot()
BufferedImage screenshot = scrollshot.screenshot();
//Save the screenshot
ImageIO.write(screenshot, "png", new File("myscreenshot.png"));
```

#### TODOs
* Publish as maven package
* Support vertical scrolling
* Test on all browsers
* More unit tests