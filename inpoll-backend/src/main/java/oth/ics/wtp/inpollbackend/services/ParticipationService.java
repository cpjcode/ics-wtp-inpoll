package oth.ics.wtp.inpollbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oth.ics.wtp.inpollbackend.dtos.AnswerDto;
import oth.ics.wtp.inpollbackend.dtos.ParticipationDto;
import oth.ics.wtp.inpollbackend.dtos.SubmitAnswerDto;
import oth.ics.wtp.inpollbackend.entities.*;
import oth.ics.wtp.inpollbackend.exceptions.*;
import oth.ics.wtp.inpollbackend.repositories.ParticipationRepository;
import oth.ics.wtp.inpollbackend.repositories.PollRepository;
import oth.ics.wtp.inpollbackend.services.commonutils.ServiceUtils;

import java.util.List;


@Service
@Transactional
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final PollRepository pollRepository;
    private final ServiceUtils serviceUtils;

    @Autowired
    public ParticipationService(ParticipationRepository participationRepository, PollRepository pollRepository,  ServiceUtils serviceUtils) {
            this.participationRepository = participationRepository;
            this.pollRepository = pollRepository;
            this.serviceUtils = serviceUtils;
        }

    private ParticipationDto toDto(Participation p) {
        return new ParticipationDto(
                p.getId(),
                p.getParticipant().getId(),
                p.getParticipant().getUsername(),
                p.getPoll().getId()
        );
    }

    public ParticipationDto participateInPoll(Long pollId, User user) { // F-P2 Users can participate in a poll
        Poll poll = serviceUtils.getPollOrThrow(pollId);

        if (!pollRepository.existsByIdAndInvitedUsersId(pollId,user.getId())) { // check if the user is invited
            throw new UninvitedUserException();
        }

        if (poll.getStatus() == PollStatus.FINISHED) { // check if poll is still active
            throw new PollAlreadyFinishedException(pollId);
        }

        Participation existing = participationRepository
                .findByPollIdAndParticipantId(pollId, user.getId())
                .orElse(null);

        if (existing != null) {
            return toDto(existing);
        }

        Participation participation = new Participation();
        participation.setPoll(poll);
        participation.setParticipant(user);

        Participation saved = participationRepository.save(participation);

        return toDto(participation);
    }

    public ParticipationDto submitParticipation(Long pollId, User user, List<SubmitAnswerDto> answersDto) { // F-P2 User submits their response.
        Poll poll = serviceUtils.getPollOrThrow(pollId);

        if (poll.getStatus() == PollStatus.FINISHED) {
            throw new PollAlreadyFinishedException(pollId);
        }

        Participation participation = participationRepository // find participation
                .findByPollIdAndParticipantId(pollId, user.getId())
                .orElseThrow(() -> new ParticipationNotFoundException(pollId, user.getId()));

        List<Question> questions = poll.getQuestions();

        if (answersDto.size() != questions.size()) { // validate answer count
            throw new InvalidAnswerCountException();
        }

        for (Question question : questions) {

            boolean found = false;

            for (SubmitAnswerDto answer : answersDto) {
                if (question.getId().equals(answer.questionId())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new UnansweredQuestionsException();
            }
        }

        for (SubmitAnswerDto dto : answersDto) {

            Question question = questions.stream()
                    .filter(q -> q.getId().equals(dto.questionId()))
                    .findFirst()
                    .orElseThrow();

            Answer answer = new Answer();
            answer.setText(dto.text());
            answer.setQuestion(question);
            answer.setParticipation(participation);

            participation.getAnswers().add(answer);
        }

        Participation saved = participationRepository.save(participation);
        return toDto(participation);
    }

}
