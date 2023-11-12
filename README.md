# Примеры запросов для основных операций
## Получаем 10 самых популярных фильмов
```	
SELECT f.id, f.title, COUNT(l.like_id) AS like_count
FROM films AS f
LEFT JOIN likes AS l ON f.id = l.film_id
GROUP BY f.id, f.title
ORDER BY like_count DESC
LIMIT 10;
```

## Выводим полный список подтвержденных друзей пользователя
```
SELECT u.id, u.name
FROM user AS u
JOIN friendship AS f ON (u.id = f.user1_id OR u.id = f.user2_id)
WHERE (f.user1_id = (id пользователя) OR f.user2_id = (id пользователя))
AND f.status = true;
```

![ER-диаграмма.](https://i.ibb.co/d74GNKQ/database.png)
