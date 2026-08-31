package oth.ics.wtp.inpollbackend.exceptions;

public class UninvitedUserException extends RuntimeException {
    public UninvitedUserException() {
        super("You are not invited to this poll");
    }
}
