package oth.ics.wtp.inpollbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.inpollbackend.entities.Participation;
import oth.ics.wtp.inpollbackend.entities.Poll;
import oth.ics.wtp.inpollbackend.entities.User;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends CrudRepository<Participation, Long> {
    boolean existsByPollIdAndParticipantId(Long pollId, Long participantId);
    Optional<Participation> findByPollIdAndParticipantId(Long pollId, Long participantId);
    List<Participation> findByPollId(Long pollId);
}
