package oth.ics.wtp.inpollbackend.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Username already exists");
    }
}
