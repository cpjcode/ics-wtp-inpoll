package oth.ics.wtp.inpollbackend.services;

import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oth.ics.wtp.inpollbackend.dtos.CreateUserDto;
import oth.ics.wtp.inpollbackend.dtos.UserDto;
import oth.ics.wtp.inpollbackend.entities.User;
import oth.ics.wtp.inpollbackend.exceptions.UnauthorizedUserException;
import oth.ics.wtp.inpollbackend.exceptions.UserAlreadyExistsException;
import oth.ics.wtp.inpollbackend.exceptions.UserNotFoundException;
import oth.ics.wtp.inpollbackend.repositories.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override @NullMarked
    public UserDetails loadUserByUsername(String username) { // F-U2. Login/authentication lookup
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public UserDto createUser(CreateUserDto dto) { // F-U1. User registration
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = new User(dto.username(), passwordEncoder.encode(dto.password()));

        User saved = userRepository.save(user);

        return new UserDto(saved.getId(), saved.getUsername());
    }

    public User getCurrentUser() { // Current user retrieval. Used by services.
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedUserException();
        }

        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
    }
}
