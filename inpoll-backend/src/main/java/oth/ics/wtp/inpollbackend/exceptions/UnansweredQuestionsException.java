package oth.ics.wtp.inpollbackend.exceptions;

public class UnansweredQuestionsException extends RuntimeException {
    public UnansweredQuestionsException() {
        super("All questions must be answered.");
    }
}
