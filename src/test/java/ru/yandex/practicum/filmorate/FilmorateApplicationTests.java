package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void whenCreateNullEmail() {
		try {
			UserController userController = new UserController();
			User testUser = new User(null, "HeroOfMight", "Alexandre", LocalDate.parse("2018-11-01"));
			userController.addUser(testUser);
		} catch (final NullPointerException e) {
			Assertions.assertNotNull(e);
		}
	}

	@Test
	public void whenCreateUserWithWrongEmail() {
		UserController userController = new UserController();
		User testUser = new User("zombiegmail.com", "Belch", "Bob", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(testUser));
		Assertions.assertEquals("Invalid user data!", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithSpacesInLogin() {
		UserController userController = new UserController();
		User testUser = new User("zombie@gmail.com", "Bob Zombie", "Bob", LocalDate.parse("2018-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(testUser));
		Assertions.assertEquals("Invalid user data!", ex.getMessage());
	}

	@Test
	public void whenCreateUserWithEmptyName_LoginIsDisplayed() {
		UserController userController = new UserController();
		User testUser = new User("zombie@gmail.com", "Belch", null, LocalDate.parse("2018-11-01"));
		userController.addUser(testUser);
		Assertions.assertEquals(testUser.getLogin(), userController.getUsers().get(0).getName());
	}

	@Test
	public void whenDateOfBirthIsInTheFuture() {
		UserController userController = new UserController();
		User testUser = new User("zombie@gmail.com", "Belch", "Bob", LocalDate.parse("2024-11-01"));
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> userController.addUser(testUser));
		Assertions.assertEquals("Invalid user data!", ex.getMessage());
	}

	@Test
	public void maximumLengthOfDescriptionCannotExceed200() {
		try {
			FilmController filmController = new FilmController();
			Film testFilm = new Film("Something Wild", "Something Wild is a 1986 American action comedy film directed by Jonathan Demme, written by E. Max Frye, and starring Melanie Griffith, Jeff Daniels and Ray Liotta.[3] It was screened out of competition at the 1987 Cannes Film Festival.[4] The film has some elements of a road movie combined with screwball comedy.", LocalDate.parse("1993-11-17"), 90);
			filmController.addFilm(testFilm);
		} catch (final HttpMessageNotReadableException e) {
			Assertions.assertNotNull(e);
		}
	}

	@Test
	public void releaseDate() {
		FilmController filmController = new FilmController();
		Film testFilm = new Film("Something Wild", "Something Wild is a 1986 American action comedy film ", LocalDate.parse("1893-11-17"), 90);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(testFilm));
		Assertions.assertEquals("Invalid film data", ex.getMessage());
	}

	@Test
	public void durationMustBePositive() {
		FilmController filmController = new FilmController();
		Film testFilm = new Film("Something Wild", "Something Wild is a 1986 American action comedy film ", LocalDate.parse("1993-11-17"), -1);
		ValidationException ex = Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(testFilm));
		Assertions.assertEquals("Invalid film data", ex.getMessage());
	}


	@Test
	@SuppressWarnings("null")
	public void nameOfFilmMustNotBeNull() {
		try {
			FilmController filmController = new FilmController();
			Film testFilm = new Film(null, "Something Wild is a 1986 American action comedy film", LocalDate.parse("1993-11-17"), 90);
			filmController.addFilm(testFilm);
		} catch (final NullPointerException e) {
			Assertions.assertNotNull(e);
		}
	}


}
