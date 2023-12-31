package cz.qjetta.birthdays.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cz.qjetta.birthdays.entities.Person;

public interface IPersonRepository extends JpaRepository<Person, Long> {

	List<Person> findAllByOrderById();

}
