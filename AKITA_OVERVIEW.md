# Akita — BDD библиотека для автоматизации тестирования

> **Источник:** https://github.com/alfa-laboratory/akita
> **Дата:** 2026-07-01

---

## Что такое Akita?

**Akita** (стилизовано как «Akita — бери и тестируй») — это BDD-библиотека шагов для автоматизации тестирования, разработанная в **Альфа-Банке** (alfa-laboratory). Она построена поверх Cucumber и Selenide и позволяет писать тесты на **русском языке** в виде пользовательских сценариев.

Проект является Open Source (Apache 2.0) и доступен на GitHub:  
👉 [https://github.com/alfa-laboratory/akita](https://github.com/alfa-laboratory/akita)

---

## Ключевые возможности

### 1. BDD-тесты на русском языке

Тесты пишутся в формате Gherkin на русском языке. Это позволяет сценариям выступать в качестве живой документации приложения.

```gherkin
Функционал: Главная страница
  Сценарий: Загрузка главной страницы
    Допустим совершен переход на страницу "Главная" по ссылке "mainPageUrl"
    Когда страница "Главная" загрузилась
    Тогда в заголовке страницы присутствует текст "OldBoy Barbershop"
```

### 2. Готовые библиотеки шагов

Akita предоставляет набор готовых шагов (step definitions) из пакета `ru.alfabank.steps`:

| Категория | Класс | Назначение |
|-----------|-------|------------|
| UI-шаги | `DefaultSteps` | Навигация, клики, ввод текста, проверки |
| API-шаги | `DefaultApiSteps` | REST-запросы (GET, POST, PUT, DELETE) |
| Предустановки | `InitialSetupSteps` | Хуки: создание/закрытие браузера, скриншоты |

### 3. Page Object Model (AkitaPage)

Для описания страниц используется паттерн Page Object. Каждая страница наследуется от `AkitaPage`:

```java
@Name("Главная")
public class MainPage extends AkitaPage {
    @FindBy(css = ".hero-title")
    @Name("Hero-заголовок")
    public SelenideElement heroTitle;
}
```

### 4. Хранилище переменных (AkitaScenario)

Позволяет сохранять и передавать значения между шагами одного сценария:

```java
akitaScenario.setVar("responseData", response);
Object data = akitaScenario.getVar("responseData");
```

### 5. Кастомный WebDriver (CustomDriverProvider)

- Поддержка Chrome, Firefox, Safari
- Headless-режим (`-Pheadless=true`)
- Удалённый запуск (`-PremoteUrl`)
- Мобильная эмуляция (MobileChrome)
- Blacklist для блокировки нежелательных ресурсов
- Proxy (BrowserMobProxy) для перехвата трафика

### 6. REST-тестирование

Встроенная поддержка отправки HTTP-запросов и проверки ответов.

### 7. Galen Framework Integration

Проверка вёрстки страниц (desktop, tablet, mobile) через Galen Spec Language.

### 8. Screenshots и отчёты

- Скриншоты после каждого шага (`takeScreenshotAfterSteps=true`)
- Кастомный StepFormatter для красивых отчётов
- Генерация HTML-отчётов через `generateCucumberReport`

---

## Архитектура проекта (шаблон)

Проект на основе Akita имеет следующую структуру:

```
project-root/
├── build.gradle                     // Сборка, зависимости, настройки тестов
├── settings.gradle                  // Название проекта
├── gradle.properties                // Параметры (браузер, удалённый хост и др.)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── pages/               // Page Object классы (AkitaPage)
│   │   │   ├── steps/               // Пользовательские шаги
│   │   │   ├── entities/            // DTO / модели данных
│   │   │   └── helpers/             // Вспомогательные классы
│   │   └── resources/
│   │       ├── application.properties // Настройки приложения
│   │       ├── restBodies/          // Шаблоны тел запросов
│   │       └── blacklist            // Блокируемые ресурсы
│   └── test/
│       └── resources/
│           ├── features/            // Gherkin .feature файлы
│           └── specs/               // Galen .spec файлы
└── gradle/wrapper/                  // Gradle Wrapper
```

---

## Запуск тестов

```bash
# Локальный запуск
./gradlew clean test -i

# С браузером и тегом
./gradlew clean test -Pbrowser=chrome -Ptag=@smoke

# Генерация отчёта
./gradlew clean generateCucumberReport

# Удалённый запуск
./gradlew clean test -Pbrowser=chrome -PremoteUrl=http://remote/wd/hub
```

---

## Преимущества Akita

| Преимущество | Описание |
|:-------------|:---------|
| 🚀 **Быстрый старт** | Используйте готовые шаги из коробки |
| 📖 **Живая документация** | Сценарии = документация |
| 🇷🇺 **Русский язык** | Понятно заказчикам и аналитикам |
| 🔌 **Расширяемость** | Легко добавлять свои шаги и Page Objects |
| 🖥 **Кросбраузерность** | Chrome, Firefox, Safari + удалённый запуск |
| 📊 **Отчёты** | Красивые HTML-отчёты со скриншотами |
| 🧪 **API + UI** | Тестируйте и фронт, и бэк в одном сценарии |

---

## Шаблон для старта

Для быстрого старта рекомендуется использовать шаблон:  
👉 [https://github.com/alfa-laboratory/akita-testing-template](https://github.com/alfa-laboratory/akita-testing-template)

## Контакты

- Telegram: [@akitaQA](https://t.me/akitaQA)
- GitHub: [alfa-laboratory/akita](https://github.com/alfa-laboratory/akita)
