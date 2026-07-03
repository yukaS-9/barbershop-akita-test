package steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;

import java.time.Duration;

/**
 * Базовые шаги для BDD-тестов (аналог ru.alfabank.steps.DefaultSteps из Akita).
 */
public class DefaultSteps {

    private static final int TIMEOUT_SECONDS = 30;

    @Допустим("совершен переход на страницу {string} по ссылке {string}")
    public void navigateToPage(String pageName, String urlKey) {
        String url = getUrlFromKey(urlKey);
        Selenide.open(url);
    }

    @Тогда("страница {string} загрузилась")
    public void pageShouldBeLoaded(String pageName) {
        WebDriverRunner.getWebDriver().getTitle();
    }

    @И("страница загрузилась")
    public void pageLoaded() {
        Selenide.sleep(1000);
    }

    @Когда("выполнено нажатие на (?:кнопку|элемент) {string}")
    public void clickButton(String elementName) {
        SelenideElement element = findElement(elementName);
        element.shouldBe(Condition.interactable, Duration.ofSeconds(TIMEOUT_SECONDS));
        element.click();
    }

    @И("выполнено нажатие на клавиатуре {string}")
    public void pressKey(String key) {
        switch (key.toLowerCase()) {
            case "enter":
                Selenide.actions().sendKeys(org.openqa.selenium.Keys.ENTER).perform();
                break;
            case "escape":
            case "esc":
                Selenide.actions().sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();
                break;
            default:
                throw new IllegalArgumentException("Неизвестная клавиша: " + key);
        }
    }

    @И("(?:элемент|секция|блок) {string} отображается на странице")
    public void elementShouldBeVisible(String elementName) {
        SelenideElement element = findElement(elementName);
        element.shouldBe(Condition.visible, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    @И("кнопка {string} отображается на странице")
    public void buttonShouldBeVisible(String buttonName) {
        SelenideElement element = findElement(buttonName);
        element.shouldBe(Condition.visible, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    @И("ссылка {string} отображается на странице")
    public void linkShouldBeVisible(String linkName) {
        SelenideElement element = findElement(linkName);
        element.shouldBe(Condition.visible, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    @И("в заголовке страницы присутствует текст {string}")
    public void checkPageTitle(String expectedText) {
        String title = WebDriverRunner.getWebDriver().getTitle();
        MatcherAssert.assertThat(
            "Заголовок страницы должен содержать: " + expectedText,
            title,
            Matchers.containsString(expectedText)
        );
    }

    @И("страница содержит текст {string}")
    public void pageContainsText(String expectedText) {
        SelenideElement body = Selenide.$(By.tagName("body"));
        String bodyText = body.getText();
        MatcherAssert.assertThat(
            "Страница должна содержать текст: " + expectedText,
            bodyText,
            Matchers.containsString(expectedText)
        );
    }

    /**
     * Поиск элемента по имени на текущей странице.
     */
    private SelenideElement findElement(String elementName) {
        SelenideElement byLinkText = Selenide.$(By.linkText(elementName));
        if (byLinkText.exists()) {
            return byLinkText;
        }
        SelenideElement byPartialText = Selenide.$(By.partialLinkText(elementName));
        if (byPartialText.exists()) {
            return byPartialText;
        }
        return Selenide.$(By.xpath("//*[contains(text(), '" + elementName + "')]"));
    }

    /**
     * Получение URL по ключу.
     */
    private static String getUrlFromKey(String key) {
        String url = System.getProperty(key);
        if (url != null && !url.isEmpty()) {
            return url;
        }
        url = System.getenv(key);
        if (url != null && !url.isEmpty()) {
            return url;
        }
        if ("mainPageUrl".equals(key)) {
            return "https://oldboybarbershop.com";
        }
        throw new IllegalArgumentException("URL не найден для ключа: " + key);
    }
}
