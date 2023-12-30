package cz.qjetta.birthdays;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cz.qjetta.birthdays.controller.PersonController;
import cz.qjetta.birthdays.model.Person;

/**
 * A few tests against {@link PersonController} without mocking so they run
 * against the database defined in profile test.
 */
@SpringBootTest
@ActiveProfiles("test")
class PersonControllerAutowiredTests {
	@Autowired
	private PersonController controller;

	private static final List<Person> LIST_10_PERSON = IntStream
			.rangeClosed(1, 10).mapToObj(i -> {
				Person person = personAtIndex(i);
				return person;
			}).collect(Collectors.toList());

	private static Person personAtIndex(int i) {
		Person person = new Person();
		person.setId((long) i);
		person.setName("Name " + i);
		person.setSurname("Surname " + i);
		person.setBirthday(LocalDate.of(2000 + i, 1, 1));
		return person;
	}

	@Test
	void getPersonList() {
		List<Person> result = controller.getPersonList();
		assertIterableEquals(LIST_10_PERSON, result);
	}

}
