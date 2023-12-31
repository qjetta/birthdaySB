package cz.qjetta.birthdays.requests;

import lombok.Data;

@Data
public class UserToRegisterRequest {
	private String name;
	private String email;
	private String password;
}
