package com.yms.auth_service.services.implementations;

import com.yms.auth_service.config.UserDetailsImpl;
import com.yms.auth_service.entities.User;
import com.yms.auth_service.enums.UserStatus;
import com.yms.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> dbUser= userRepository.findByEmailAndStatus(username, UserStatus.ACTIVE);
        if (dbUser.isEmpty())
            throw new UsernameNotFoundException("User not found with email : "+username);
        User user = dbUser.get();

        return new UserDetailsImpl(user);
    }

}
