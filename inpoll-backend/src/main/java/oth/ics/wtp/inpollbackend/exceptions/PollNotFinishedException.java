package oth.ics.wtp.inpollbackend.exceptions;

public class PollNotFinishedException extends RuntimeException {
    public PollNotFinishedException(Long pollId) {
        super("Poll with id " + pollId + " is not finished yet");
    }
}
