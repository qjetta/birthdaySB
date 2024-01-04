package cz.qjetta.birthdays.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import cz.qjetta.birthdays.entities.Person;

public class PersonSpecification {

	public static Specification<Person> isBirthdayLessThan(
			LocalDate hardcodedDate) {
		return (root, query, cb) -> {
			return cb.lessThan(root.get("birthday"), hardcodedDate);
		};
	}

	public static Specification<Person> isBirthdayGreaterThan(
			LocalDate hardcodedDate) {
		return (root, query, cb) -> {
			return cb.greaterThan(root.get("birthday"), hardcodedDate);
		};
	}

	public static Specification<Person> nameLike(String searchText) {
		return (root, query, cb) -> cb.like(root.get("name"),
				"%" + searchText + "%");
	}

}
