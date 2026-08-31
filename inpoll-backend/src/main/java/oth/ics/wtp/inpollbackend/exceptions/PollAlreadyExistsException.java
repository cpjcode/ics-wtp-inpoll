package oth.ics.wtp.inpollbackend.exceptions;

public class PollAlreadyExistsException extends RuntimeException {
    public PollAlreadyExistsException(String title) {
        super("Poll with title " + title + " already exists");
    }
}
