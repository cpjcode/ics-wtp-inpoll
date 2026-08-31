package oth.ics.wtp.inpollbackend;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import oth.ics.wtp.inpollbackend.dtos.CreateUserDto;
import oth.ics.wtp.inpollbackend.services.UserService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class InPollControllerTestBase {

    protected static final String USER_ROLE = "USER";

    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeEach() {

        userService.createUser(new CreateUserDto("u1", "ignored"));
        userService.createUser(new CreateUserDto("u2", "ignored"));
        userService.createUser(new CreateUserDto("creator", "ignored"));

    }

    protected void authenticateAs(String username) {

        UserDetails user = User.builder()
                .username(username)
                .password("ignored")
                .roles("USER")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    protected void authenticateAsUser1() {
        authenticateAs("u1");
    }

    protected void authenticateAsUser2() {
        authenticateAs("u2");
    }

    protected void authenticateAsCreator() {
        authenticateAs("creator");
    }

}
