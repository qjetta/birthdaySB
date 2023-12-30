package cz.qjetta.birthdays.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cz.qjetta.birthdays.config.RecordNotFoundException;
import cz.qjetta.birthdays.model.Person;
import cz.qjetta.birthdays.repository.IPersonRepository;

@Service
public class PersonService {
	private IPersonRepository personRepository;

	public PersonService(IPersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Person save(Person person) {
		return personRepository.save(person);
	}

	public List<Person> findAll() {
		return personRepository.findAllByOrderById();
	}

	public void delete(Long id) {
		if (!personRepository.existsById(id)) {
			throw new RecordNotFoundException();
		}
		personRepository.deleteById(id);
	}

	public Person findById(Long id) {

		Optional<Person> person = personRepository.findById(id);
		if (person.isPresent()) {
			return person.get();
		}
		throw new RecordNotFoundException();
	}

}
