# PoizonX Telegram Bot

Этот телеграм бот разработан для удобного расчета стоимости заказа при использовании посреднических услуг на китайской платформе Poizon. Основная задача бота - предоставить пользователям быстрый доступ к информации о полной стоимости заказа, включая комиссии сервиса, затраты на логистику и текущий обменный курс.

## Основная функциональность

1. **Расчет стоимости заказа:** Пользователь вводит в бота стоимость товара в юанях, а бот возвращает полную стоимость заказа до доставки в Москву, учитывая актуальный обменный курс и комиссии сервиса.

2. **Ответы на часто задаваемые вопросы:** Бот обладает набором команд, которые предоставляют ответы на часто задаваемые вопросы пользователей или отправляют ссылки на статьи с подробными объяснениями.

3. **Разгрузка менеджеров сервиса PoizonX:** Использование бота позволяет снизить нагрузку на менеджеров сервиса, освобождая их от рутиных запросов на расчет стоимости заказов.

## Технологии

- **Java:** Язык программирования, используемый для разработки бота.
- **Spring Boot:** Фреймворк для создания веб-приложений на языке Java, облегчающий разработку и управление приложением.
- **MySQL:** Реляционная база данных, используемая для хранения информации о заказах и пользователях.
- **Telegram API:** Интерфейс программирования приложений, предоставляемый Telegram для создания ботов.
  
## Установка и запуск

1. Клонируйте репозиторий на свой компьютер.
2. Установите необходимые зависимости, указанные в `pom.xml`.
3. Настройте базу данных MySQL и укажите соответствующие параметры в файле `application.properties`.
4. Получите токен от Telegram Bot API и укажите его в `application.properties`.
5. Запустите приложение.
6. Добавьте бота в свой телеграм аккаунт и начните использовать.

## Как использовать

1. Для расчета стоимости заказа отправьте боту сообщение с указанием стоимости товара в юанях.
2. Используйте доступные команды для получения ответов на вопросы или ссылок на статьи.


