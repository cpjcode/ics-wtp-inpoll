package oth.ics.wtp.inpollbackend.exceptions;

public class SelfInviteException extends RuntimeException {
    public SelfInviteException() {
        super("Users cannot invite themselves");
    }
}
