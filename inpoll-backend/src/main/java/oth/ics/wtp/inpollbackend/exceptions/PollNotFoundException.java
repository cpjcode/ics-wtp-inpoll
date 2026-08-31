package oth.ics.wtp.inpollbackend.exceptions;

public class PollNotFoundException extends RuntimeException {
    public PollNotFoundException(Long id) {
        super("Poll with id " + id + " not found");
    }
}
