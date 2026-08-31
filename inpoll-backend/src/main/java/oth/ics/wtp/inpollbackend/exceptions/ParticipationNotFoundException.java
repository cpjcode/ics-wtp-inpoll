package oth.ics.wtp.inpollbackend.exceptions;

public class ParticipationNotFoundException extends RuntimeException {
    public ParticipationNotFoundException(Long pollId, Long userId) {
        super("Participation not found for user " + userId + " in poll " + pollId);
    }
}