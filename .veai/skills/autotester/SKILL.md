---
name: autotester
description: Правила и паттерны для написания UI/API автотестов на основе BDD-фреймворка Akita (Cucumber + Selenide). Применять при автоматизации тестирования веб-приложений.
allowed-tools:
  - Read
  - Write
  - Edit
  - Bash
  - RunConfiguration
---

# /autotester — Правила написания тестов на основе Akita

## 1. Общая архитектура

Akita — BDD-библиотека шагов от Альфа-Банка, построенная поверх **Cucumber** (Gherkin) и **Selenide**.  
Тесты пишутся на **русском языке** и представляют собой пользовательские сценарии = живую документацию.

### Ключевые компоненты

| Компонент | Назначение |
|-----------|------------|
| `AkitaPage` | Базовый класс для Page Object. Содержит методы работы с элементами |
| `AkitaScenario` | Хранилище переменных в рамках одного сценария |
| `DefaultSteps` | Готовые шаги для UI: навигация, клики, ввод, проверки |
| `DefaultApiSteps` | Готовые шаги для REST: GET, POST, PUT, DELETE |
| `InitialSetupSteps` | Хуки: создание/закрытие браузера, скриншоты |
| `CustomDriverProvider` | Кастомный WebDriver с поддержкой Chrome, Firefox, Safari, headless, remote |
| `PropertyLoader` | Загрузка параметров из `application.properties` |
| `YamlDataLoader` | Загрузка тестовых данных из YAML-файлов |

---

## 2. Структура проекта

```
project-root/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── pages/               # Page Object (AkitaPage)
│   │   │   ├── steps/               # Пользовательские шаги
│   │   │   └── helpers/             # Хелперы
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── restBodies/          # Шаблоны тел запросов
│   │       └── blacklist            # Блокируемые ресурсы
│   └── test/
│       └── resources/
│           ├── features/            # Gherkin .feature файлы
│           └── specs/               # Galen .spec файлы
└── gradle/wrapper/
```

---

## 3. Page Object (AkitaPage)

Каждая страница наследуется от `AkitaPage` и описывается с аннотацией `@Name`.

### Правила:
1. **Страница** — класс с аннотацией `@Name("Имя на русском")`, extends `AkitaPage`
2. **Элементы** — поля типа `SelenideElement` или `List<SelenideElement>` с аннотациями:
   - `@FindBy(css = "...")` — локатор
   - `@Name("Название на русском")` — имя для поиска в шагах
   - `@Optional` — элемент не обязателен для загрузки страницы
   - `@Hidden` — элемент не должен отображаться при загрузке
3. **Инициализация**: `page.initialize().appeared()` — проверяет загрузку всех обязательных элементов
4. **Доступ**: `akitaScenario.getCurrentPage().getElement("Имя элемента")`
5. **Блоки**: можно описывать вложенные блоки на странице через `@FindBy` + `@Name`

### Пример:

```java
@Name("Главная")
public class MainPage extends AkitaPage {
    @FindBy(css = ".hero-title")
    @Name("Hero-заголовок")
    public SelenideElement heroTitle;

    @Optional
    @FindBy(css = ".optional-banner")
    @Name("Баннер (необязательный)")
    public SelenideElement optionalBanner;

    @Hidden
    @FindBy(css = ".service-list")
    @Name("Список услуг")
    public List<SelenideElement> serviceList;
}
```

---

## 4. Хранилище переменных (AkitaScenario)

**Назначение:** передача значений между шагами одного сценария.

### Методы:
- `akitaScenario.setVar("key", value)` — сохранить
- `akitaScenario.getVar("key")` — прочитать
- `akitaScenario.setCurrentPage(page)` — установить текущую страницу
- `akitaScenario.getCurrentPage()` — получить текущую страницу
- `akitaScenario.getPage(PageClass.class)` — получить конкретную страницу
- `akitaScenario.write("сообщение")` — записать в отчёт

### Использование в feature-файлах:
Переменные подставляются через `{key}` или `<key>` в шагах.

---

## 5. Написание Gherkin-сценариев

### Правила:
- **Язык:** русский, `#language:ru` в начале .feature файла
- **Ключевые слова:** `Функционал:`, `Сценарий:`, `Дано`, `Когда`, `Тогда`, `И`
- **Теги:** `@smoke`, `@p0`, `@p1`, `@regress` и т.д. — для фильтрации запусков
- **Имена шагов** должны совпадать с регулярными выражениями в step definitions
- **Ссылки** на элементы — по русским именам из `@Name`

### Пример:
```gherkin
#language:ru

Функционал: Главная страница
  Сценарий: Загрузка главной страницы
    Допустим совершен переход на страницу "Главная" по ссылке "mainPageUrl"
    Когда страница "Главная" загрузилась
    Тогда в заголовке страницы присутствует текст "OldBoy Barbershop"
    И элемент "Hero-заголовок" отображается на странице
```

### Стандартные готовые шаги (из DefaultSteps):
| Шаг | Описание |
|-----|----------|
| `совершен переход на страницу "{name}" по ссылке "{key}"` | Навигация по URL из properties |
| `страница "{name}" загрузилась` | Ожидание загрузки страницы |
| `выполнено нажатие на кнопку "{name}"` | Клик по элементу |
| `в заголовке страницы присутствует текст "{text}"` | Проверка title |
| `страница содержит текст "{text}"` | Проверка body text |
| `элемент "{name}" отображается на странице` | Проверка видимости |
| `выполнен {method} запрос на URL "{url}" ...` | REST-запрос |

---

## 6. WebDriver / Запуск тестов

### Параметры запуска (через `-P` или `-D`):
```bash
# Локально
./gradlew clean test -i

# Выбор браузера
./gradlew clean test -Pbrowser=chrome

# Headless режим
./gradlew clean test -Pheadless=true

# Удалённый запуск
./gradlew clean test -PremoteUrl=http://remote/wd/hub

# Фильтр по тегам
./gradlew clean test -Ptag=@smoke

# Размер окна
./gradlew clean test -Pwidth=1200 -Pheight=800

# Генерация отчёта
./gradlew clean generateCucumberReport
```

### Blacklist:
Файл `src/main/resources/blacklist` (без расширения) — блокировка ресурсов:
```
.*ru.fp.kaspersky-labs.com.*
http://google.com/ 200
```
Формат: `URL [статус_код]` (по умолчанию 404).

### Proxy:
Для включения BrowserMobProxy: `-Dproxy=true`

---

## 7. application.properties

Файл в `src/main/resources/` для хранения:
- URL (`mainPageUrl=https://example.com`)
- Таймаутов (`waitingAppearTimeout=150000`)
- Параметров браузера
- Har-файлов для proxy

---

## 8. REST-тестирование

Готовые шаги в `DefaultApiSteps`:
```gherkin
Когда выполнен POST запрос на URL "{url}" с headers и parameters из таблицы. Полученный ответ сохранен в переменную
    | type   | name          | value           |
    | header | Content-Type  | application/json |
    | body   | requestBody   | <fileForCreate>  |
```

---

## 9. YAML-тестовые данные

Файлы в `src/main/resources/` (*.yml). Загрузка:
```java
// Чтение в JsonNode
JsonNode data = YamlDataLoader.readDataToJsonNode("testData.yml", "alfabank");

// Десериализация в POJO
BankInfo info = YamlDataLoader.readDataToClassType("testData.yml", BankInfo.class, "alfabank");

// Слияние нескольких файлов
JsonNode merged = YamlDataLoader.mergeNodes(commonSettings, devSettings);
```

---

## 10. Скриншоты и отчёты

- **После каждого шага:** `takeScreenshotAfterSteps=true`
- **После конкретного шага:** аннотация `@Screenshot` над step definition
- **Кастомный StepFormatter:** `ru.alfabank.tests.core.formatters.StepFormatter`
- **Вывод в отчёт:** `akitaScenario.write("Текст для отчёта")`

---

## 11. Проверка логических выражений

```gherkin
Тогда верно что "amountToPay == amountMonthly + penalty + 100"
```

Поддерживаются `==` (равно) и `!=` (не равно).

---

## 12. Galen Framework (опционально)

Проверка вёрстки через `.spec` файлы в `src/test/resources/specs/`.
Поддерживаются разрешения: desktop, tablet, mobile.

---

## 13. Рекомендации по стилю

1. **Именование.** Page Object — по названию страницы (например, `MainPage`). Feature-файлы — по функционалу (`smoke.feature`, `auth.feature`).
2. **Теги.** Использовать иерархию: `@smoke @p0` (критические), `@smoke @p1` (важные), `@regress` (полный регресс).
3. **Переменные.** Все URL и конфиги — в `application.properties`, доступ через ключи. Не хардкодить URL в шагах.
4. **Step Definitions.** Если часто повторяется комбинация из нескольких стандартных шагов — вынести в кастомный шаг.
5. **Page Object.** Каждый элемент должен иметь осмысленное русское имя в `@Name`. Необязательные элементы — `@Optional`. Скрытые — `@Hidden`.

---
## 14. Зависимости для build.gradle

dependencies {
// Selenide — управление браузером
implementation 'com.codeborne:selenide:6.19.1'

    // Cucumber — BDD-тесты
    implementation 'io.cucumber:cucumber-java:7.15.0'
    implementation 'io.cucumber:cucumber-junit:7.15.0'
    implementation 'io.cucumber:cucumber-picocontainer:7.15.0'

    // Тестирование
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.2'
    testImplementation 'org.junit.vintage:junit-vintage-engine:5.10.2'

    // JUnit 4 — для Cucumber runner
    testImplementation 'junit:junit:4.13.2'

    // Логирование
    implementation 'org.slf4j:slf4j-simple:2.0.12'

    // Lombok
    compileOnly 'org.projectlombok:lombok:1.18.32'
    annotationProcessor 'org.projectlombok:lombok:1.18.32'

    // Hamcrest — матчеры для assert
    implementation 'org.hamcrest:hamcrest:2.2'
}