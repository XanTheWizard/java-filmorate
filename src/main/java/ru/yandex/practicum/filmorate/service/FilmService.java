package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    public UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }


    public Film addFilm(Film film) { //Create new film
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) { //Update an existing movie
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilmsList() { //Generate a list of movies
        return new ArrayList<>(filmStorage.getFilms());
    }

    public Film getFilmById(int id) { //Get a movie by its id
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            return film;
        } else {
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such movie! Film id = " + id);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "There is no such movie! Film id = " + id);
        }
    }


    //user likes the movie
    public void like(int filmId, int userId) {
        userService.getUserById(userId);
        getFilmById(filmId).getLikes().add(userId);
    }

    //user deletes like
    public void unlike(int filmId, int userId) {
        userService.getUserById(userId);
        getFilmById(filmId).getLikes().remove(userId);
    }

    public List<Film> getMoviesByLikes(int count) {
        List<Film> sortedList = new ArrayList<>(getFilmsList());
        FilmsByLikesComparator filmsByLikesComparator = new FilmsByLikesComparator();
        sortedList.sort(filmsByLikesComparator);
        if (count == 0) {
            return sortedList.stream().limit(10).collect(Collectors.toList());
        } else {
            return sortedList.stream().limit(count).collect(Collectors.toList());
        }
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)) || film.getDuration() <= 0) {
            throw new ValidationException("Invalid film data!");
        }
    }

    private static class FilmsByLikesComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            if (film1.getLikes().size() > film2.getLikes().size()) {
                return -1;
            } else if (film1.getLikes().size() < film2.getLikes().size()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
