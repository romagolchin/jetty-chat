* Сделать интерфейс для AccountService

// Context: DBService, AccountService
* Unit тесты для сервисов
---
* Поддержать несколько чатов
* Создание чата
* Приглашение участников
* Удаление участников
* Выход из чата

#### Реализация
* БД
     * храним для пользователя список чатов
     * для чата - список пользователей
     * для сообщения - пользователя (не логин) и чат
 * изменить ChatService, ChatWebSocket


* хранить информацию о тестовых и настоящих конфигурациях БД каждую в 1м файле так, чтобы можно было легко переключаться
* файл должно быть удобно парсить
* научиться дропать все после теста
* переписать получение `SessionFactory`

* убедиться, что запускается тест
* mysql system time
* написать removeUser
* frontend
