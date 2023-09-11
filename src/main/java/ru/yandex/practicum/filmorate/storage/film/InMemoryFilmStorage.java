package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private int filmId;

    public HashMap<Integer, Film> getFilms() {
        return new HashMap<Integer, Film>(films);
    }

    public Film addFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            film.setId(++filmId);
            films.put(film.getId(), film);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Film id = " + film.getId() + " already exists!");
        }
        return film;
    }

    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no such film id = " + film.getId());
        }
    }


    public Film getFilmById(int id) {
        return films.get(id);
    }

    public void deleteFilm(int id) {
        films.remove(id);
    }
}
