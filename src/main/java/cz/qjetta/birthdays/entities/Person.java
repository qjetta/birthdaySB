package cz.qjetta.birthdays.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String name;
	private String surname;

	@PastOrPresent
	@NotNull
	private LocalDate birthday;

}
