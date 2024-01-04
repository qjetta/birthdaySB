package cz.qjetta.birthdays.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewPersonRequest {

	private String name;
	private String surname;

	@PastOrPresent
	@NotNull
	private LocalDate birthday;

}
