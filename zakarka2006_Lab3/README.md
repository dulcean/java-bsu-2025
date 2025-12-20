# Lab 3

Ну короче из толкового я смог вспомнить про сокращалки ссылок, поэтому вот моя простенькая имплементация этого сервиса.

## Запуск

```bash
docker-compose up -d --build
```

После запуска:
- **Frontend**: http://localhost
- **Backend API**: http://localhost:8080
- **Spy Page (CORS Demo)**: http://localhost:81

## Архитектура

щя будет

## Эндпоинты

| Method | Endpoint | Описание |
|--------|----------|----------|
| `POST` | `/api/links` | Создать короткую ссылку |
| `GET` | `/api/links` | Получить все ссылки |
| `GET` | `/api/links/{code}` | Информация о ссылке |
| `DELETE` | `/api/links/{code}` | Удалить ссылку |
| `GET` | `/{code}` | Редирект на оригинальный URL |
