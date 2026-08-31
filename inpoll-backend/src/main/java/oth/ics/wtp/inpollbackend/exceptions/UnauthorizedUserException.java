package oth.ics.wtp.inpollbackend.exceptions;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super("Unauthorized user");
    }
}
