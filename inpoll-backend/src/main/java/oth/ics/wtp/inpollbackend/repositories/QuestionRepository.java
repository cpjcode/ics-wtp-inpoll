package oth.ics.wtp.inpollbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import oth.ics.wtp.inpollbackend.entities.Question;

public interface QuestionRepository extends CrudRepository<Question, Long> {
}
