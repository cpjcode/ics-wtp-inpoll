package oth.ics.wtp.inpollbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.inpollbackend.entities.Poll;
import oth.ics.wtp.inpollbackend.entities.User;

import java.util.List;

public interface PollRepository extends CrudRepository<Poll, Long> {
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, Long id);
    List<Poll> findByInvitedUsersContains(User user);
    boolean existsByIdAndInvitedUsersId(Long pollId, Long invitedUsersId);
    List<Poll> findByCreatorId(Long creatorId);
}
