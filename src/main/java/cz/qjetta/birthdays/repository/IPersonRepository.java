package cz.qjetta.birthdays.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cz.qjetta.birthdays.entities.Person;

public interface IPersonRepository
		extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

	@Override
	Page<Person> findAll(Specification<Person> specification, Pageable page);
}
