package cz.qjetta.birthdays.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.qjetta.birthdays.entities.Person;
import cz.qjetta.birthdays.requests.NewPersonRequest;
import cz.qjetta.birthdays.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RequestMapping(path = "person")
@RestController
@Slf4j
@Tag(name = "Person Controller")
public class PersonController {

	private PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@Operation(summary = "Create a person", description = "Creates a person in the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successfully created"),
			@ApiResponse(responseCode = "404", description = "Not found") })
	@PostMapping
	public ResponseEntity<Person> createPerson(
			@RequestBody NewPersonRequest newPerson) {
		if (log.isDebugEnabled()) {
			log.debug("create person: %s".formatted(newPerson));
		}
		Person person = new Person(null, newPerson.getName(),
				newPerson.getSurname(), newPerson.getBirthday());
		return new ResponseEntity<>(personService.save(person),
				HttpStatus.CREATED);
	}

	@Operation(summary = "Returs a list of people", description = "Returns list of people with its details based on the parameters ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "People successfully found") })
	@GetMapping
	public Page<Person> getPersonList(
			@Parameter(description = "Person name", example = "Name") @RequestParam(required = false) String name,
			@Parameter(description = "Return people that has birthday after specified date", example = "2005-01-01") @RequestParam(required = false) LocalDate birthdayAfter,
			@Parameter(description = "Return people that has birthday before specified date", example = "2008-01-01") @RequestParam(required = false) LocalDate birthdayBefore,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size) {
		return personService.findAll(name, birthdayBefore, birthdayAfter,
				PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
	}

	@Operation(summary = "Get a person by id", description = "Returns a person")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully found"),
			@ApiResponse(responseCode = "404", description = "Not found") })
	@GetMapping(path = "{id}")
	public Person getPerson(
			@Parameter(description = "Person id", example = "1") @PathVariable Long id) {
		return personService.findById(id);
	}

	@Operation(summary = "Delete a person by id", description = "Deletes a person in the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully deleted"),
			@ApiResponse(responseCode = "404", description = "Person not found") })
	@DeleteMapping(path = "{id}")
	public void deletePerson(
			@Parameter(description = "Person id", example = "1") @PathVariable Long id) {
		personService.delete(id);
	}
}
