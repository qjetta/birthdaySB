package cz.qjetta.birthdays.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
	@Schema(example = "user", requiredMode = RequiredMode.REQUIRED)
	private String username;
	@Schema(example = "password", requiredMode = RequiredMode.REQUIRED)
	private String password;

}
