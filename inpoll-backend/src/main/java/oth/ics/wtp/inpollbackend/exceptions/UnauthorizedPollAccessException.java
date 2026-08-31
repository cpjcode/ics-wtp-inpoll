package oth.ics.wtp.inpollbackend.exceptions;

public class UnauthorizedPollAccessException extends RuntimeException {
    public UnauthorizedPollAccessException() {
        super("You are not allowed to modify this poll");
    }
}
