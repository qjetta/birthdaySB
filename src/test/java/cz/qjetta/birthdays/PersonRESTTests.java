package cz.qjetta.birthdays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.qjetta.birthdays.requests.AuthRequests;
import cz.qjetta.birthdays.requests.PersonRequests;

/**
 * Test directly REST API against H2 database that is defined in test profile
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonRESTTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testAuthorisationMethods() throws Exception {
		AuthRequests authReq = new AuthRequests(mockMvc, objectMapper);

		authReq.loginNotAuthorized(12);
		authReq.loginNotAuthorized(13);
		authReq.registerUser(12);
		authReq.login(12);
		authReq.loginNotAuthorized(13);
	}

	@Test
	void testPersonMethods() throws Exception {
		AuthRequests authReq = new AuthRequests(mockMvc, objectMapper);
		PersonRequests persReq = new PersonRequests(mockMvc, objectMapper);

		persReq.getPersonListNotAuthorised();
		persReq.postPersonNotAuthorised(1);
		persReq.deletePersonNotAuthorised(1);

		authReq.registerUser(2);
		String jwt = authReq.login(2);

		persReq.getPersonList(PersonRequests.LIST_10_PERSON, jwt);

		persReq.getPersonList(PersonRequests.LIST_10_PERSON, jwt);
		persReq.getPerson(1, 10, jwt);
		persReq.getPersonNotFound(11, 20, jwt);

		persReq.postPerson(11, jwt);
		persReq.getPersonList(PersonRequests.LIST_11_PERSON, jwt);
		persReq.getPerson(1, 11, jwt);

		persReq.deletePerson(11, jwt);
		persReq.getPersonList(PersonRequests.LIST_10_PERSON, jwt);
	}
}
