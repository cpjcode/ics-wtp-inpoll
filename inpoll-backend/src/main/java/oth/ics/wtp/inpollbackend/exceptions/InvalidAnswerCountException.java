package oth.ics.wtp.inpollbackend.exceptions;

public class InvalidAnswerCountException extends RuntimeException {
    public InvalidAnswerCountException() {
        super("Number of answers does not match number of questions");
    }
}