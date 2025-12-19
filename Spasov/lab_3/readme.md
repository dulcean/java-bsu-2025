![](tessia.jpg)

Ответы на вопросики:

## Java и docker
Java - для кроссплатформенности и написания fizzbuzz enterprise 

Docker - для контейнеризации и одинакового поведения, незавимо от платформы.

## Почему прога
Почему захотел стать программистом - никем не хотел стать, просто прикольно и легче, чем где-то

## Мелкомягкие
Сложно сказать, что ненавижу. Много софта адаптировано нормально под винду, и у меня батарейка на ней живет дольше. 
Винда как домашняя ось меня устраивает. 

Gcc на винде через жопу, как и многое, если мне не изменяет память работает.
Но я это не учитываю, для кодинга на убунте сижу.

## Любимый препод

@sokolik - контракты, китайские машины, стендап

## Запуск

```bash
sudo docker-compose up --build
```

Основной интерфейс: `http://localhost:8080`
Шпионская панель: `http://localhost:8080/spy`

## Схемка

```
       [Клиент]
           |
           | HTTP POST/GET (JSON)
           v
[API Gateway: Docker Network / Port 8080]
           |
           v
[Backend: Spring Boot 3.3] ----------------------.
   |                   \                         |
   |                    \                        |
   |             [Logic: Java 17 + JNA]          |
   |          1. JPA/Hibernate Cache Check       |
   |          2. HttpURLConnection (Trace)       |
   |          3. Regex / String Transformation   |
   |                      |                      |
   v                      v                      v
[Persistence: PostgreSQL] <--- [Final URL]   [Response: REST]
   | (SQL / Native Queries)                      |
   |                                             v
   |                  [Output: Image Display + UI Feedback]
   |                                             |
   v                                             v
[Spy Dashboard: /spy] <--------------------------'
   |
   |-- [Data Log: List<Waifu> via JPA]
   '-- [Analytics: SQL GROUP BY + Projections Interface]
```