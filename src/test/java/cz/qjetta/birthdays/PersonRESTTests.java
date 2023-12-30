package cz.qjetta.birthdays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.qjetta.birthdays.model.Person;

/**
 * Test directly REST API against H2 database that is defined in test profile
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonRESTTests {
	private static final String ENDPOINT_PATH = "/person";
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private static final List<Person> LIST_10_PERSON = initPersonList(10);
	private static final List<Person> LIST_11_PERSON = initPersonList(11);

	private static List<Person> initPersonList(int maxIndex) {
		return IntStream.rangeClosed(1, maxIndex).mapToObj(i -> {
			Person person = personAtIndex(i, true);
			return person;
		}).collect(Collectors.toList());
	}

	private static Person personAtIndex(int i, boolean generateId) {
		Person person = new Person();
		if (generateId) {
			person.setId((long) i);
		}
		person.setName("Name " + i);
		person.setSurname("Surname " + i);
		person.setBirthday(LocalDate.of(2000 + i, 1, 1));
		return person;
	}

	@Test
	void testPersonMethods() throws Exception {
		getPersonList(LIST_10_PERSON);
		getPerson(1, 10);
		getPersonNotFound(11, 20);

		postPerson(11);
		getPersonList(LIST_11_PERSON);
		getPerson(1, 11);

		deletePerson(11);
		getPersonList(LIST_10_PERSON);
	}

	void getPersonList(List<Person> expectedList) throws Exception {
		String expectedBody = objectMapper.writeValueAsString(expectedList);

		mockMvc.perform(get(ENDPOINT_PATH))//
				.andExpectAll(status().isOk(), content().json(expectedBody));
	}

	void getPerson(int beginIndex, int endIndex) throws Exception {
		for (long index = beginIndex; index <= endIndex; index++) {
			String expectedBody = objectMapper
					.writeValueAsString(personAtIndex((int) index, true));

			mockMvc.perform(get(ENDPOINT_PATH + "/" + index))//
					.andExpectAll(status().isOk(),
							content().json(expectedBody));
		}
	}

	void getPersonNotFound(int beginIndex, int endIndex) throws Exception {
		for (long index = beginIndex; index <= endIndex; index++) {

			mockMvc.perform(get(ENDPOINT_PATH + "/" + index))//
					.andExpectAll(status().isNotFound());
		}
	}

	void postPerson(int newIndex) throws Exception {
		String requestBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, false));
		String expectedResponseBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, true));

		mockMvc.perform(post(ENDPOINT_PATH)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isCreated(),
						content().json(expectedResponseBody));

	}

	void deletePerson(int index) throws Exception {

		mockMvc.perform(delete(ENDPOINT_PATH + "/" + index))//
				.andExpect(status().isOk());
	}

}
