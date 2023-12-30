package cz.qjetta.birthdays.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.qjetta.birthdays.model.Person;
import cz.qjetta.birthdays.service.PersonService;
import lombok.extern.slf4j.Slf4j;

@RequestMapping(path = "person")
@RestController
@Slf4j
public class PersonController {

	private PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@PostMapping
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		if (log.isDebugEnabled()) {
			log.debug("create person: %s".formatted(person));
		}
		return new ResponseEntity<>(personService.save(person),
				HttpStatus.CREATED);
	}

	@GetMapping
	public List<Person> getPersonList() {
		return personService.findAll();
	}

	@GetMapping(path = "{id}")
	public Person getPerson(@PathVariable Long id) {
		return personService.findById(id);
	}

	@DeleteMapping(path = "{id}")
	public void deletePerson(@PathVariable Long id) {
		personService.delete(id);
	}
}
