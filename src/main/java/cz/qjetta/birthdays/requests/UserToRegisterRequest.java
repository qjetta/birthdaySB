package cz.qjetta.birthdays.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@Data
public class UserToRegisterRequest {
	@Schema(example = "user", requiredMode = RequiredMode.REQUIRED)
	private String name;
	@Schema(example = "user@example.com", requiredMode = RequiredMode.REQUIRED)
	private String email;
	@Schema(example = "password", requiredMode = RequiredMode.REQUIRED)
	private String password;
}
