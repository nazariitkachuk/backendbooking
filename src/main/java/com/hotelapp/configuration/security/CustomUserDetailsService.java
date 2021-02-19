package com.hotelapp.configuration.security;

import com.hotelapp.entities.User;
import com.hotelapp.exceptions.DataNotFoundException;
import com.hotelapp.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @NonNull
    private UserRepository userRepository;

    /**
     * NOT USED
     * @param username in plaintext
     * @return implementation of UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User with username: " + username + " does not exist"));
        return new CustomUserDetails(user);
    }

    /**
     *
     * @param userId identyfying user
     * @return implementation of UserDetails
     */
    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id: " + userId + " does not exist"));
        return new CustomUserDetails(user);
    }
}
