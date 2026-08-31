package oth.ics.wtp.inpollbackend.exceptions;

public class AlreadyParticipatedException extends RuntimeException {
    public AlreadyParticipatedException(Long userId, Long pollId) {
        super("User with id " + userId + " has already participated in poll with id " + pollId);
    }
}
