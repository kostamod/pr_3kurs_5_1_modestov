# Практическое занятие 5.1 — Погода

Готовый учебный Android-проект на Kotlin. Выполнены запрос к OpenWeatherMap через Volley, разбор JSON через Gson, вывод города, температуры, давления и скорости ветра, запись в Logcat. Есть своя иконка, название, тема и портретная ориентация.

## Версии

- Android Studio Hedgehog 2023.1.1 или более новая версия, поддерживающая AGP 8.2.
- Android Gradle Plugin 8.2.0, Gradle 8.2, Kotlin 1.9.10.
- Gradle JDK 17 (в Hedgehog входит в комплект).
- Android SDK Platform 34 и Android SDK Build-Tools 34.0.0.
- Для устройства или эмулятора: Android 6.0 (API 23) или новее.

Версии сборки взяты из примера https://github.com/kostamod/modestovk_PR-31_v2. Исходный пример содержит другое приложение; эта практика написана отдельно по заданию.

## Запуск в ПТУ

1. Распакуйте архив в короткий путь, например `C:\AndroidProjects\WeatherPractice51`. Не открывайте проект внутри ZIP.
2. Откройте Android Studio → Open → папка `WeatherPractice51`, где лежит `settings.gradle.kts`.
3. Дождитесь синхронизации. При первом запуске нужен интернет для Gradle и библиотек.
4. В Settings → Build, Execution, Deployment → Build Tools → Gradle выберите Gradle JDK 17. В SDK Manager установите Android SDK Platform 34 и Build-Tools 34.0.0, если их нет.
5. В Device Manager создайте эмулятор Android API 23+ либо подключите телефон с отладкой по USB. Нажмите Run ▶, модуль app.
6. В приложении нажмите «Демо без интернета». На экране появятся учебные данные Екатеринбурга: 12,5 °C, 1013 гПа, 3,2 м/с. Ввод города не меняет демоданные.
7. Для реального запроса зарегистрируйтесь на https://home.openweathermap.org/users/sign_up и получите ключ на https://home.openweathermap.org/api_keys. Введите его в поле приложения, укажите город и нажмите «Получить погоду из API». Новый ключ может активироваться не сразу.
8. Для демонстрации задания откройте Logcat и задайте фильтр `tag:WeatherAPI level:DEBUG`. В старом Logcat выберите уровень Debug и найдите WeatherAPI. В строке будут город и температура, а также давление и ветер. Источник помечен как API либо ДЕМО.

Ключ не встроен в проект, не записывается на диск и не выводится в Logcat. После закрытия приложения его нужно вводить заново. Рабочий ключ нельзя заменить деморежимом при сдаче именно сетевого запроса.

## Если в кабинете нет интернета

Первую синхронизацию и сборку сделайте заранее на этом же компьютере. ZIP с исходниками не включает SDK, Android Studio и кэш зависимостей. Для установки без Android Studio используйте APK, если он приложен к архиву, либо соберите его заранее: Build → Build Bundle(s) / APK(s) → Build APK(s). Результат: `app/build/outputs/apk/debug/app-debug.apk`. После установки кнопка «Демо» работает без сети. Настоящая погода требует интернета и ключа.

## Сборка из командной строки Windows

В папке проекта: `gradlew.bat assembleDebug`. Требуются JDK 17 и Android SDK. Android Studio сама создаёт `local.properties` с путём SDK. Этот файл не переносите с другого компьютера.

## Структура

- `app/src/main/java/ru/student/weather51/MainActivity.kt` — обработчики кнопок, Volley, Gson и Logcat.
- `app/src/main/java/ru/student/weather51/WeatherResponse.kt` — классы JSON.
- `app/src/main/res/layout/activity_main.xml` — экран приложения.
- `app/src/main/res/drawable/` — иконка и фон карточки.
- `app/src/main/res/values/` — название и тема.
- `app/src/main/assets/weather_demo.json` — учебный JSON.
- `app/src/main/AndroidManifest.xml` — INTERNET, запуск, портретная ориентация.
- `app/build.gradle.kts` — зависимости и версии SDK.
- `gradle/wrapper/`, `gradlew`, `gradlew.bat` — Gradle Wrapper.
- `docs/Отчет.html` — отчёт, открывается в браузере, печатается через Ctrl+P; можно открыть в Word.
- `docs/Проверка.md` — порядок проверки и реальные результаты проверки сборки.

## Что показывать преподавателю

Покажите экран с реальной погодой и строку WeatherAPI в Logcat. Затем покажите JSON-модель, функцию loadWeather(), Gson.fromJson(), INTERNET в манифесте, зависимости и XML-разметку. Продемонстрируйте обработку пустого города и неверного ключа.

ROOM упомянута в цели методички, но отдельного задания на таблицы, DAO или хранение данных нет. В этой работе база не добавлена: приложение реализует конкретные пункты задания 1 и отчёт из задания 2.

## Частые проблемы

- Ошибка JDK: выберите именно JDK 17 для Gradle, а не меняйте версии проекта наугад.
- SDK location not found: откройте проект в Android Studio и задайте установленный SDK в SDK Manager.
- Не загружаются зависимости: проверьте сеть и доступ к Google Maven, Maven Central и services.gradle.org. Для первой сборки отключите Gradle Offline mode.
- HTTP 401: проверьте собственный API-ключ и его активацию.
- HTTP 404: исправьте город, например `Moscow,RU`.
- После ошибки старые данные остаются на экране с предупреждением, что они не обновлены.
