package oth.ics.wtp.inpollbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import oth.ics.wtp.inpollbackend.controllers.UserController;
import oth.ics.wtp.inpollbackend.dtos.CreateUserDto;
import oth.ics.wtp.inpollbackend.dtos.UserDto;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest extends InPollControllerTestBase {

    @Autowired
    private UserController userController;

    @Test
    public void testCreateUser() {

        CreateUserDto request = new CreateUserDto(
                "newUser",
                "password123"
        );

        UserDto created = userController.createUser(request);

        assertNotNull(created);
        assertEquals("newUser", created.username());
        assertNotNull(created.id());
    }

    @Test
    void testGetCurrentUser() {

        authenticateAsUser1();
        UserDto currentUser = userController.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals("u1", currentUser.username());
    }

    @Test
    void testCreateDuplicateUserShouldFail() {

        CreateUserDto request = new CreateUserDto(
                "u1",
                "ignored"
        );

        assertThrows(Exception.class, () -> {
            userController.createUser(request);
        });
    }

}
