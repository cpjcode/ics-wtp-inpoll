package oth.ics.wtp.inpollbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.inpollbackend.dtos.CreateUserDto;
import oth.ics.wtp.inpollbackend.dtos.UserDto;
import oth.ics.wtp.inpollbackend.entities.User;
import oth.ics.wtp.inpollbackend.services.UserService;

@SecurityRequirement(name = "basicAuth")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserDto dto) { // F-U1
        return userService.createUser(dto);
    }

    @GetMapping("/users/current")
    public UserDto getCurrentUser() { // F-U2 support method. Get currently logged-in user
        User user = userService.getCurrentUser();

        return new UserDto(
                user.getId(),
                user.getUsername()
        );
    }
}
