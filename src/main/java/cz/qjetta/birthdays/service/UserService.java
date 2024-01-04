package cz.qjetta.birthdays.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cz.qjetta.birthdays.entities.UserInfo;
import cz.qjetta.birthdays.exception.UniqueException;
import cz.qjetta.birthdays.repository.UserInfoRepository;

@Service
public class UserService {
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserInfoRepository repository;

	public void addUser(UserInfo userInfo) {
		if (userInfo == null) {
			throw new IllegalArgumentException();
		}

		if (userInfo.getId() != null
				&& repository.existsById(userInfo.getId())) {
			throw new UniqueException("Id", userInfo.getId().toString());
		}
		if (userInfo.getName() != null
				&& repository.existsByName(userInfo.getName())) {
			throw new UniqueException("Name", userInfo.getName());
		}
		if (userInfo.getEmail() != null
				&& repository.existsByEmail(userInfo.getEmail())) {
			throw new UniqueException("Email", userInfo.getEmail());
		}

		System.err.println(encoder.encode(userInfo.getPassword()));

		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		repository.save(userInfo);
	}
}
