package cz.qjetta.birthdays.requests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.qjetta.birthdays.entities.UserInfo;

public class AuthRequests {
	static final String BEARER = "Bearer ";

	private static final String REGISTER_ENDPOINT_PATH = "/register";
	private static final String LOGIN_ENDPOINT_PATH = "/login";

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	public AuthRequests(MockMvc mockMvc, ObjectMapper objectMapper) {
		super();
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}

	private static UserInfo userAtIndex(int i, boolean generateId) {
		UserInfo userInfo = new UserInfo();
		if (generateId) {
			userInfo.setId(i);
		}
		userInfo.setName("user" + i);
		userInfo.setPassword("password" + i);
		userInfo.setEmail("email" + i + "@example.com");
		return userInfo;
	}

	public void registerUser(int newIndex) throws Exception {
		String requestBody = objectMapper
				.writeValueAsString(userAtIndex(newIndex, false));

		mockMvc.perform(post(REGISTER_ENDPOINT_PATH)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isCreated());
	}

	public String login(int newIndex) throws Exception {
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

	public void loginNotAuthorized(int newIndex) throws Exception {
		UserInfo userAtIndex = userAtIndex(newIndex, true);
		AuthRequest authRequest = new AuthRequest();
		authRequest.setUsername(userAtIndex.getName());
		authRequest.setPassword(userAtIndex.getPassword());

		String requestBody = objectMapper.writeValueAsString(authRequest);

		mockMvc.perform(post(LOGIN_ENDPOINT_PATH)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpectAll(status().isForbidden());
	}

}
