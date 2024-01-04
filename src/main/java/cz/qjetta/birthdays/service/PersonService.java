package cz.qjetta.birthdays.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cz.qjetta.birthdays.entities.Person;
import cz.qjetta.birthdays.exception.RecordNotFoundException;
import cz.qjetta.birthdays.repository.IPersonRepository;
import cz.qjetta.birthdays.specification.PersonSpecification;

@Service
public class PersonService {
	private IPersonRepository personRepository;

	public PersonService(IPersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Person save(Person person) {
		return personRepository.save(person);
	}

	public Page<Person> findAll(String name, LocalDate birthdayLessThan,
			LocalDate birthdayGreaterThan, Pageable pageable) {

		if (name == null && birthdayLessThan == null
				&& birthdayGreaterThan == null) {
			return personRepository.findAll(pageable);
		}
		List<Specification<Person>> l = new ArrayList<>();
		if (name != null)
			l.add(PersonSpecification.nameLike(name));
		if (birthdayGreaterThan != null)
			l.add(PersonSpecification
					.isBirthdayGreaterThan(birthdayGreaterThan));
		if (birthdayLessThan != null)
			l.add(PersonSpecification.isBirthdayLessThan(birthdayLessThan));

		Specification<Person> spec = l.get(0);
		for (int i = 1; i < l.size(); i++) {
			spec = spec.and(l.get(i));
		}

		return personRepository.findAll(Specification.where(spec), pageable);
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
