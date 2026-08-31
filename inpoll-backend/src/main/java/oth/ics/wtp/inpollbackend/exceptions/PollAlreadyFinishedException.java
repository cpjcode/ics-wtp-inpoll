package oth.ics.wtp.inpollbackend.exceptions;

public class PollAlreadyFinishedException extends RuntimeException {
    public PollAlreadyFinishedException(Long id) { super("Poll with id " + id + " is already finished"); }
}
