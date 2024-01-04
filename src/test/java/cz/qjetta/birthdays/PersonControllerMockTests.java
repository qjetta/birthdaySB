package cz.qjetta.birthdays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

	private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0,
			100, Sort.by(Sort.Direction.ASC, "id"));
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

		Page<Person> foundPage = new PageImpl<Person>(LIST_10_PERSON,
				DEFAULT_PAGE_REQUEST, 10);

		IPersonRepository mockRepository = mock(IPersonRepository.class);
		when(mockRepository.findAll(DEFAULT_PAGE_REQUEST))
				.thenReturn(foundPage);
		PersonService service = new PersonService(mockRepository);
		PersonController controller = new PersonController(service);
		List<Person> result = controller.getPersonList(null, null, null, 0, 100)
				.toList();
		assertIterableEquals(foundPage, result);
	}

	@Test
	void getPersonListFiltered() {

		for (int i = 1; i <= 10; i++) {
			Page<Person> foundPage = new PageImpl<Person>(
					List.of(personAtIndex(i)), DEFAULT_PAGE_REQUEST, 1);

			PersonController controller = getMockControllerFindAll(foundPage);
			List<Person> result = controller
					.getPersonList("Name " + i, null, null, 0, 100).toList();
			assertIterableEquals(foundPage, result);
		}

	}

	private PersonController getMockControllerFindAll(Page<Person> foundPage) {
		IPersonRepository mockRepository = mock(IPersonRepository.class);
		Page<Person> findAll = mockRepository.findAll(any(Specification.class),
				any(Pageable.class));

		when(findAll).thenReturn(foundPage);
		PersonService service = new PersonService(mockRepository);
		PersonController controller = new PersonController(service);
		return controller;
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
