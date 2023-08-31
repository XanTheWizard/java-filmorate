package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int id;

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            validateFilm(film);
            ++id;
            film.setId(id);
            films.put(id, film);
        } else {
            throw new ValidationException("Id фильма совпадает с имеющимся в базе");
        }
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Такой фильм ещё не был добавлен");
        }
    }

    @GetMapping("/getfilms")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) || film.getDuration() <= 0) {
            throw new ValidationException("Неверные данные: слишком ранняя дата выпуска или отрицательная длинна");

        }
    }
}
