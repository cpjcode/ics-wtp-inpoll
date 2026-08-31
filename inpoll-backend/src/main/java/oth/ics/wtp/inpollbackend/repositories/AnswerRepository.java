package oth.ics.wtp.inpollbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.inpollbackend.entities.Answer;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    boolean existsByParticipation_Poll_IdAndParticipation_Participant_Id(Long pollId, Long participantId);
}
