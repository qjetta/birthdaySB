package cz.qjetta.birthdays.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.qjetta.birthdays.model.Person;
import cz.qjetta.birthdays.service.PersonService;

@RequestMapping(path = "person")
@RestController
public class PersonController {
	private PersonService personService;

	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@PostMapping
	public void createPerson(Person person) {
		personService.save(person);
	}

	@GetMapping
	public List<Person> getPersonList() {
		return personService.findAll();
	}

	@GetMapping(path = "{id}")
	public Person getPerson(@PathVariable Long id) {
		return personService.findById(id);
	}

	@DeleteMapping
	public void deletePerson(Long id) {
		personService.delete(id);
	}
}
