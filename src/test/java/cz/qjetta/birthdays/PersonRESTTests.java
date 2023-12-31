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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.qjetta.birthdays.entities.Person;
import cz.qjetta.birthdays.entities.UserInfo;
import cz.qjetta.birthdays.requests.AuthRequest;

/**
 * Test directly REST API against H2 database that is defined in test profile
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonRESTTests {
	private static final String BEARER = "Bearer ";
	private static final String PERSON_ENDPOINT_PATH = "/person";
	private static final String REGISTER_ENDPOINT_PATH = "/register";
	private static final String LOGIN_ENDPOINT_PATH = "/login";
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

	private static UserInfo userAtIndex(int i, boolean generateId) {
		UserInfo userInfo = new UserInfo();
		if (generateId) {
			userInfo.setId(i);
		}
		userInfo.setName("user" + i);
		userInfo.setPassword("password" + i);
		userInfo.setEmail("email" + 1 + "@example.com");
		return userInfo;
	}

	@Test
	void testPersonMethods() throws Exception {
		getPersonListNotAuthorised();
		postPersonNotAuthorised(1);
		deletePersonNotAuthorised(1);

		registerUser(2);
		String jwt = login(2);

		getPersonList(LIST_10_PERSON, jwt);

		getPersonList(LIST_10_PERSON, jwt);
		getPerson(1, 10, jwt);
		getPersonNotFound(11, 20, jwt);

		postPerson(11, jwt);
		getPersonList(LIST_11_PERSON, jwt);
		getPerson(1, 11, jwt);

		deletePerson(11, jwt);
		getPersonList(LIST_10_PERSON, jwt);

	}

	private void registerUser(int newIndex) throws Exception {
		String requestBody = objectMapper
				.writeValueAsString(userAtIndex(newIndex, false));

		mockMvc.perform(post(REGISTER_ENDPOINT_PATH)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isCreated());

	}

	private String login(int newIndex) throws Exception {
		UserInfo userAtIndex = userAtIndex(newIndex, true);
		AuthRequest authRequest = new AuthRequest();
		authRequest.setUsername(userAtIndex.getName());
		authRequest.setPassword(userAtIndex.getPassword());

		String requestBody = objectMapper.writeValueAsString(authRequest);

		MvcResult mvcResult = mockMvc.perform(post(LOGIN_ENDPOINT_PATH)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isOk()).andReturn();

		// jwt
		return mvcResult.getResponse().getContentAsString();

	}

	void getPersonList(List<Person> expectedList, String jwt) throws Exception {
		String expectedBody = objectMapper.writeValueAsString(expectedList);

		mockMvc.perform(get(PERSON_ENDPOINT_PATH)
				//
				.header("Authorization", BEARER + jwt))
				// .with(SecurityMockMvcRequestPostProcessors.jwt()))//
				.andExpectAll(status().isOk(), content().json(expectedBody));
	}

	void getPersonListNotAuthorised() throws Exception {
		mockMvc.perform(get(PERSON_ENDPOINT_PATH))//
				.andExpectAll(status().isForbidden());
	}

	void getPerson(int beginIndex, int endIndex, String jwt) throws Exception {
		for (long index = beginIndex; index <= endIndex; index++) {
			String expectedBody = objectMapper
					.writeValueAsString(personAtIndex((int) index, true));

			mockMvc.perform(get(PERSON_ENDPOINT_PATH + "/" + index)
					.header("Authorization", BEARER + jwt))//
					.andExpectAll(status().isOk(),
							content().json(expectedBody));
		}
	}

	void getPersonNotFound(int beginIndex, int endIndex, String jwt)
			throws Exception {
		for (long index = beginIndex; index <= endIndex; index++) {

			mockMvc.perform(get(PERSON_ENDPOINT_PATH + "/" + index)
					.header("Authorization", BEARER + jwt))
					.andExpectAll(status().isNotFound());
		}
	}

	void postPerson(int newIndex, String jwt) throws Exception {
		String requestBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, false));
		String expectedResponseBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, true));

		mockMvc.perform(post(PERSON_ENDPOINT_PATH)
				.header("Authorization", BEARER + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isCreated(),
						content().json(expectedResponseBody));
	}

	void postPersonNotAuthorised(int newIndex) throws Exception {
		String requestBody = objectMapper
				.writeValueAsString(personAtIndex(newIndex, false));

		mockMvc.perform(post(PERSON_ENDPOINT_PATH)

				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isForbidden());
	}

	void deletePerson(int index, String jwt) throws Exception {

		mockMvc.perform(delete(PERSON_ENDPOINT_PATH + "/" + index)
				.header("Authorization", BEARER + jwt))
				.andExpect(status().isOk());
	}

	void deletePersonNotAuthorised(int index) throws Exception {

		mockMvc.perform(delete(PERSON_ENDPOINT_PATH + "/" + index))
				.andExpect(status().isForbidden());
	}

}
