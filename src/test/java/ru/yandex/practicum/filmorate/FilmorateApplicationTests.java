package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void whenCreateNullEmail() {
		try {
			InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
			UserService userService = new UserService(inMemoryUserStorage);
			User testUser = new User(null, "HeroOfMight", "Alexandre", LocalDate.parse("2018-11-01"));
			userService.addUser(testUser);
		} catch (final NullPointerException e) {
			Assertions.assertNotNull(e);
		}
	}

	@Test
	public void whenCreateUserWithWrongEmail() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("zombiegmail.com", "Belch", "Bob", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userService.addUser(testUser));
		Assertions.assertEquals("Invalid user data!", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithSpacesInLogin() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("zombie@gmail.com", "Bob Zombie", "Bob", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userService.addUser(testUser));
		Assertions.assertEquals("Invalid user data!", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithEmptyName_LoginIsDisplayed() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("zombie@gmail.com", "Belch", null, LocalDate.parse("2018-11-01"));
		userService.addUser(testUser);
		Assertions.assertEquals(testUser.getLogin(), userService.getUsersList().get(0).getName());
	}

	@Test
	public void whenDateOfBirthIsInTheFuture() {
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		User testUser = new User("zombie@gmail.com", "Belch", "Bob", LocalDate.parse("2024-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userService.addUser(testUser));
		Assertions.assertEquals("Invalid user data!", ex.getMessage());
	}

	@Test
	public void maximumLengthOfDescriptionCannotExceed200() {
		try {
			InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
			InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
			UserService userService = new UserService(inMemoryUserStorage);
			FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
			Film testFilm = new Film("Something Wild", "Something Wild is a 1986 American action comedy film directed by Jonathan Demme, written by E. Max Frye, and starring Melanie Griffith, Jeff Daniels and Ray Liotta.[3] It was screened out of competition at the 1987 Cannes Film Festival.[4] The film has some elements of a road movie combined with screwball comedy.", LocalDate.parse("1993-11-17"), 90);
			filmService.addFilm(testFilm);
		} catch (final HttpMessageNotReadableException e) {
			Assertions.assertNotNull(e);
		}
	}

	@Test
	public void releaseDate() {
		InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
		Film testFilm = new Film("Something Wild", "Something Wild is a 1986 American action comedy film ", LocalDate.parse("1893-11-17"), 90);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmService.addFilm(testFilm));
		Assertions.assertEquals("Invalid film data!", ex.getMessage());
	}

	@Test
	public void durationMustBePositive() {
		InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
		InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
		UserService userService = new UserService(inMemoryUserStorage);
		FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
		Film testFilm = new Film("Something Wild", "Something Wild is a 1986 American action comedy film ", LocalDate.parse("1993-11-17"), -1);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmService.addFilm(testFilm));
		Assertions.assertEquals("Invalid film data!", ex.getMessage());
	}


	@Test
	@SuppressWarnings("null")
	public void nameOfFilmMustNotBeNull() {
		try {
			InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
			InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
			UserService userService = new UserService(inMemoryUserStorage);
			FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
			Film testFilm = new Film(null, "Something Wild is a 1986 American action comedy film", LocalDate.parse("1993-11-17"), 90);
			filmService.addFilm(testFilm);
		} catch (final NullPointerException e) {
			Assertions.assertNotNull(e);
		}
	}


}
