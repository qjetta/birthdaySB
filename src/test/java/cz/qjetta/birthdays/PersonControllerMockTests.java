package cz.qjetta.birthdays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cz.qjetta.birthdays.controller.PersonController;
import cz.qjetta.birthdays.entities.Person;
import cz.qjetta.birthdays.repository.IPersonRepository;
import cz.qjetta.birthdays.service.PersonService;

/**
 * A few tests against {@link PersonController} with mocking
 */
@SpringBootTest
@ActiveProfiles("test")
class PersonControllerMockTests {

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
		person.setBirthday(LocalDate.of(1990 + i, 1, 1));
		return person;
	}

	@Test
	void getPersonList() {
		IPersonRepository mockRepository = mock(IPersonRepository.class);
		when(mockRepository.findAllByOrderById()).thenReturn(LIST_10_PERSON);
		PersonService service = new PersonService(mockRepository);
		PersonController controller = new PersonController(service);
		List<Person> result = controller.getPersonList();
		assertIterableEquals(LIST_10_PERSON, result);
	}

	@Test
	void getPerson() {
		for (long index = 1; index <= 10; index++) {
			IPersonRepository mockRepository = mock(IPersonRepository.class);
			when(mockRepository.findById(index))
					.thenReturn(Optional.of(personAtIndex((int) index)));
			PersonService service = new PersonService(mockRepository);
			PersonController controller = new PersonController(service);
			Person result = controller.getPerson(index);
			Person expected = personAtIndex((int) index);

			assertEquals(expected, result,
					"getPerson at index %s".formatted(index));
		}
	}

}
