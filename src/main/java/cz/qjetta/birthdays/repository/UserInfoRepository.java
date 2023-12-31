package cz.qjetta.birthdays.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.qjetta.birthdays.entities.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
	Optional<UserInfo> findByName(String username);

	boolean existsByName(String name);

	boolean existsByEmail(String email);
}
