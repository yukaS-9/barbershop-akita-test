package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object для главной страницы OldBoy Barbershop.
 * Внимание: селекторы требуют уточнения под реальную вёрстку сайта.
 */
public class MainPage {

    // ========== Навигация ==========

    @FindBy(css = ".header .logo, a.navbar-brand, .logo img")
    public SelenideElement logo;

    @FindBy(css = ".header .nav, .navbar, nav.navigation, header nav")
    public SelenideElement navigationMenu;

    @FindBy(css = ".lang-switcher, .language-switcher, a[href*='lang']")
    public SelenideElement languageSwitcher;

    // ========== Hero-блок ==========

    @FindBy(css = "h1, .hero-title, .banner-title, .page-title")
    public SelenideElement heroTitle;

    @FindBy(css = ".stat-counter, .count-filials, .hero-counter, [class*='count']:not([class*='btn'])")
    public SelenideElement filialCounter;

    @FindBy(css = "a[href*='online'], a[href*='record'], .btn-record, a[href*='zapis']")
    public SelenideElement onlineBookingButton;

    @FindBy(css = "a[href*='cosmetic'], a[href*='kosmetik'], .btn-cosmetics")
    public SelenideElement buyCosmeticsButton;

    // ========== Секция услуг ==========

    @FindBy(css = ".services-section, .services-block, [class*='service'] section, #block-services")
    public SelenideElement servicesSection;

    @FindBy(css = ".service-card, .service-item, [class*='service'] .card, .service")
    public List<SelenideElement> serviceCards;

    // ========== Блок мобильного приложения ==========

    @FindBy(css = ".app-section, .mobile-app, [class*='app'], #block-app")
    public SelenideElement mobileAppSection;

    @FindBy(css = "a[href*='play.google.com'], a[href*='google.play'], a[href*='googleplay']")
    public SelenideElement googlePlayButton;

    @FindBy(css = "a[href*='apps.apple.com'], a[href*='apple.com'], a[href*='appstore']")
    public SelenideElement appStoreButton;

    // ========== Блок косметики ==========

    @FindBy(css = ".cosmetics-section, .cosmetics-block, [class*='cosmetic'] section, #block-cosmetics")
    public SelenideElement cosmeticsSection;

    @FindBy(css = "a[href*='/cosmetics'], a[href*='cosmetic']")
    public SelenideElement learnMoreLink;

    // ========== Карта филиалов ==========

    @FindBy(css = ".map-section, .filials-section, [class*='filial'], [class*='map']")
    public SelenideElement mapSection;

    @FindBy(css = "[class*='franchise'], .franchise-block")
    public SelenideElement franchiseBlock;

    @FindBy(css = "a[href*='franchis']")
    public SelenideElement openFranchiseButton;

    // ========== Footer ==========

    @FindBy(css = "footer, .footer, .page-footer")
    public SelenideElement footer;
}
