package oth.ics.wtp.inpollbackend.services.commonutils;

import org.springframework.stereotype.Component;
import oth.ics.wtp.inpollbackend.entities.Poll;
import oth.ics.wtp.inpollbackend.entities.User;
import oth.ics.wtp.inpollbackend.exceptions.InvalidArgumentException;
import oth.ics.wtp.inpollbackend.exceptions.PollNotFoundException;
import oth.ics.wtp.inpollbackend.exceptions.UnauthorizedPollAccessException;
import oth.ics.wtp.inpollbackend.repositories.PollRepository;

@Component
public class ServiceUtils {

    private final PollRepository pollRepository;

    public ServiceUtils(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public void assertOwner(Poll poll, User user) { // Authorization check
        if (!poll.getCreator().getId().equals(user.getId())) {
            throw new UnauthorizedPollAccessException();
        }
    }

    public Poll getPollOrThrow(Long id) { // Existence check.
        return pollRepository.findById(id)
                .orElseThrow(() -> new PollNotFoundException(id));
    }

    public Poll getOwnedPollOrThrow(Long id, User user) { // Shortcut combining assertOwner and getPollThrow since these 2 are used frequently together.
        Poll poll = getPollOrThrow(id);
        assertOwner(poll, user);
        return poll;
    }

    public static int mapToNumber(String value) {
        String v = value.trim();

        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid numeric answer: " + value);
        }
    }

}