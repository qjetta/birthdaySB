package cz.qjetta.birthdays.requests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.qjetta.birthdays.entities.Person;

public class PersonRequests {
	private static final String PERSON_ENDPOINT_PATH = "/person";

	public static final List<Person> LIST_10_PERSON = initPersonList(10);
	public static final List<Person> LIST_11_PERSON = initPersonList(11);

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	public PersonRequests(MockMvc mockMvc, ObjectMapper objectMapper) {
		super();
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}

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

	public void getPersonList(List<Person> expectedList, String jwt)
			throws Exception {
		String expectedBody = objectMapper.writeValueAsString(expectedList);

		mockMvc.perform(get(PERSON_ENDPOINT_PATH)//
				.header("Authorization", AuthRequests.BEARER + jwt))
				.andExpectAll(status().isOk(), content().json(expectedBody));
	}

	public void getPersonListNotAuthorised() throws Exception {
		mockMvc.perform(get(PERSON_ENDPOINT_PATH))//
				.andExpectAll(status().isForbidden());
	}

	public void getPerson(int beginIndex, int endIndex, String jwt)
			throws Exception {
		for (long index = beginIndex; index <= endIndex; index++) {
			String expectedBody = objectMapper
					.writeValueAsString(personAtIndex((int) index, true));

			mockMvc.perform(get(PERSON_ENDPOINT_PATH + "/" + index)
					.header("Authorization", AuthRequests.BEARER + jwt))//
					.andExpectAll(status().isOk(),
							content().json(expectedBody));
		}
	}

	public void getPersonNotFound(int beginIndex, int endIndex, String jwt)
			throws Exception {
		for (long index = beginIndex; index <= endIndex; index++) {

			mockMvc.perform(get(PERSON_ENDPOINT_PATH + "/" + index)
					.header("Authorization", AuthRequests.BEARER + jwt))
					.andExpectAll(status().isNotFound());
		}
	}

	public void postPerson(int newIndex, String jwt) throws Exception {
		String requestBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, false));
		String expectedResponseBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, true));

		mockMvc.perform(post(PERSON_ENDPOINT_PATH)
				.header("Authorization", AuthRequests.BEARER + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isCreated(),
						content().json(expectedResponseBody));
	}

	public void postPersonNotAuthorised(int newIndex) throws Exception {
		String requestBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, false));

		mockMvc.perform(post(PERSON_ENDPOINT_PATH)

				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isForbidden());
	}

	public void deletePerson(int index, String jwt) throws Exception {

		mockMvc.perform(delete(PERSON_ENDPOINT_PATH + "/" + index)
				.header("Authorization", AuthRequests.BEARER + jwt))
				.andExpect(status().isOk());
	}

	public void deletePersonNotAuthorised(int index) throws Exception {

		mockMvc.perform(delete(PERSON_ENDPOINT_PATH + "/" + index))
				.andExpect(status().isForbidden());
	}

}
