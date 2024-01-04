package cz.qjetta.birthdays.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cz.qjetta.birthdays.config.UserInfoDetails;
import cz.qjetta.birthdays.entities.UserInfo;
import cz.qjetta.birthdays.repository.UserInfoRepository;

@Service
public class UserInfoService implements UserDetailsService {

	@Autowired
	private UserInfoRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		Optional<UserInfo> userDetail = repository.findByName(username);

		// Converting userDetail to UserDetails
		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException(
						"User not found " + username));
	}

}
