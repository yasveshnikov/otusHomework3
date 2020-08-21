import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class YandexMarketTest {

    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(YandexMarketTest.class);

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        logger.info("Драйвер поднят");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://market.yandex.ru/");
        driver.manage().addCookie(new Cookie("oMaSefD", "1"));
        logger.info("Яндекс Маркет открыт");
        driver.get("https://market.yandex.ru/");
    }

    @Test
    public void test() {
        driver.findElement(By.cssSelector("div._3Lwc_UVFq4")).click();
        String locatorSmartphones = "div.pEcbzwIiNV:nth-child(1) > ul:nth-child(2) > li:nth-child(1) > div:nth-child(1) > a:nth-child(1)";
        new WebDriverWait(driver, 60)
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector(locatorSmartphones)));
        logger.info("Каталог открыт");
        driver.findElement(By.cssSelector(locatorSmartphones)).click();
        new WebDriverWait(driver,60)
                .until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath("//span[text()='Samsung']"))).click();
        logger.info("Выбран фильтр Samsung");
        new WebDriverWait(driver,60)
                .until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath("//span[text()='Xiaomi']"))).click();
        logger.info("Выбран фильтр Xiaomi");
        new WebDriverWait(driver,60)
                .until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath("//button[text()='по цене']"))).click();
        logger.info("Сортировка по цене(от меньшей к большей)");
        new WebDriverWait(driver, 60)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div._2LvbieS_AO._1oZmP3Lbj2")));
        logger.info("Лоадер скрылся");

        driver.findElement(By.xpath("//a[contains(@title,'Galaxy')]/ancestor::article//div[contains(@aria-label,'сравнению')]")).click();
        logger.info("Первый Samsung добавлен в сравнение");
        String popup1 = "Товар " + driver.findElement(By.xpath("//a[contains(@title,'Galaxy')]")).getAttribute("title").toString() + " добавлен к сравнению";
        String check1 = new WebDriverWait(driver,60).
                until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Galaxy')]"))).getText();
        assertEquals(popup1, check1);
        logger.info("Отобразилась плашка \"Товар Смартфон Galaxy добавлен к сравнению\"");

        driver.findElement(By.xpath("//a[contains(@title,'Redmi')]/ancestor::article//div[contains(@aria-label,'сравнению')]")).click();
        logger.info("Первый Xiaomi добавлен в сравнение");
        String popup2 = "Товар " + driver.findElement(By.xpath("//a[contains(@title,'Redmi')]")).getAttribute("title").toString() + " добавлен к сравнению";
        String check2 = new WebDriverWait(driver,60)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Redmi')]"))).getText();
        assertEquals(popup2, check2);
        logger.info("Отобразилась плашка \"Товар Смартфон Redmi добавлен к сравнению\"");

        driver.get("https://market.yandex.ru/compare?track=head");
        logger.info("Перешли в раздел Сравнение");

        List<WebElement> smartPhones = driver.findElements((By.xpath("//img[contains(@alt,'Смартфон ')]")));
        assertEquals(2, smartPhones.size());
        logger.info("В списке товаров 2 позиции");

        driver.findElement(By.xpath("//button[text()='Все характеристики']")).click();
        logger.info("Нажали на опцию \"все характеристики\"");
        driver.findElement(By.xpath("//div[text()='Операционная система']"));
        logger.info("В списке характеристик появилась позиция \"Операционная система\"");

        driver.findElement(By.xpath("//button[text()='Различающиеся характеристики']")).click();
        logger.info("Нажали на опцию \"различающиеся характеристики\"");
        assertEquals(0, driver.findElements(By.xpath("//div[text()='Операционная система']")).size());
        logger.info("Позиция \"Операционная система\" не отображается в списке характеристик");

    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
