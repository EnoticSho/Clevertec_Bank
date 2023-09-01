# Clevertec_Bank

## Описание

Клиент-серверное консольное приложение для банковских операций. Клиент обменивается с сервером сообщениями, предоставляя следующий функционал:

- Авторизация пользователя.
- Просмотр текущего баланса.
- Пополнение счёта.
- Снятие средств.
- Перевод средств на другой счёт.

Сервер, в свою очередь, обеспечивает подключение к базе данных и обработку различных транзакций. После каждой транзакции сохраняется чек в папку с проектом.

## Инструкция по запуску

1. **Настройка Docker**:
    - Запустите Docker, используя команду `docker-compose up`. Это создаст базу данных, а также выполнит создание таблиц и вставку начальных значений.

2. **Запуск сервера**:
    - Перейдите к файлу `Clevertec_Bank/Server_ClevertecBank/src/main/java/server/CleverBankApp.java` и запустите его.

3. **Запуск клиента**:
    - Перейдите к файлу `Clevertec_Bank/Client_ClevertecBank/src/main/java/clientbank/ClientRunner.java` и запустите его.