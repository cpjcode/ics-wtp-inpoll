package oth.ics.wtp.inpollbackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oth.ics.wtp.inpollbackend.dtos.*;
import oth.ics.wtp.inpollbackend.entities.*;
import oth.ics.wtp.inpollbackend.exceptions.*;
import oth.ics.wtp.inpollbackend.repositories.AnswerRepository;
import oth.ics.wtp.inpollbackend.repositories.ParticipationRepository;
import oth.ics.wtp.inpollbackend.repositories.PollRepository;
import oth.ics.wtp.inpollbackend.repositories.UserRepository;
import oth.ics.wtp.inpollbackend.services.commonutils.ServiceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class PollService {
    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final ServiceUtils serviceUtils;
    private final ParticipationRepository participationRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public PollService(PollRepository pollRepository, UserRepository userRepository, ServiceUtils serviceUtils, ParticipationRepository participationRepository, AnswerRepository answerRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.serviceUtils = serviceUtils;
        this.participationRepository = participationRepository;
        this.answerRepository = answerRepository;
    }

    private PollResponseDto toDto(Poll poll) {

        if (poll.getStatus() == PollStatus.ACTIVE
                && poll.getDueDate() != null
                && poll.getDueDate().isBefore(java.time.Instant.now())) {

            poll.setStatus(PollStatus.FINISHED);
            pollRepository.save(poll);
        }

        return new PollResponseDto(
                poll.getId(),
                poll.getTitle(),
                poll.getDueDate(),
                poll.getQuestions().size(),
                poll.getCreator().getUsername(),
                poll.getStatus()
        );
    }

    public PollResponseDto createPoll(CreatePollDto dto, User creator) { // F-C1.
        if (pollRepository.existsByTitle(dto.title())) { // Duplicate title check.
            throw new PollAlreadyExistsException(dto.title());
        }

        Poll poll =  new Poll(
                dto.title(),
                dto.description()
        );

        poll.setDueDate(dto.dueDate());
        poll.setStatus(PollStatus.ACTIVE);
        poll.setCreator(creator);

        if (dto.questions() != null) {
            for (CreateQuestionDto qdto : dto.questions()) {
                Question q = new Question(qdto.question(), qdto.type(), poll);
                poll.addQuestion(q);
            }
        }

        Poll saved =  pollRepository.save(poll);
        return toDto(saved);
    }

    public PollResponseDto updatePoll(Long pollId, UpdatePollDto dto, User creator) { // F-C4. Update the title/description/due date.
        Poll poll = serviceUtils.getOwnedPollOrThrow(pollId, creator);

        if (pollRepository.existsByTitleAndIdNot(dto.title(), pollId)) {
            throw new PollAlreadyExistsException(dto.title());
        }

        poll.setTitle(dto.title());
        poll.setDescription(dto.description());
        poll.setDueDate(dto.dueDate());

        Poll updated = pollRepository.save(poll);
        return toDto(updated);
    }

    public PollResponseDto inviteToPoll(Long pollId, String username, User creator) { // F-C2. Invite other users.
        Poll poll = serviceUtils.getOwnedPollOrThrow(pollId, creator);

        User invitedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (creator.getUsername().equals(username)) { // Creators must not invite themselves.
            throw new SelfInviteException();
        }

        poll.addInvitedUser(invitedUser);
        Poll updated = pollRepository.save(poll);

        return toDto(updated);
    }

    public PollResponseDto finishPoll(Long pollId, User creator) { // F-C3. Finish the poll.
        Poll poll = serviceUtils.getOwnedPollOrThrow(pollId, creator);

        if (poll.getStatus() == PollStatus.FINISHED) {
            throw new PollAlreadyFinishedException(pollId);
        }

        poll.setStatus(PollStatus.FINISHED);

        Poll saved = pollRepository.save(poll);
        return toDto(saved);
    }

    public List<PollResponseDto> getPendingPolls(User user) { // F-P1. Users can view the pending polls they were invited to.
        List<PollResponseDto> result = new ArrayList<>();

        for (Poll poll : pollRepository.findByInvitedUsersContains(user)) {

            if (poll.getStatus() != PollStatus.ACTIVE) {
                continue;
            }

            if (answerRepository.existsByParticipation_Poll_IdAndParticipation_Participant_Id(
                    poll.getId(), user.getId())) {
                continue;
            }

            result.add(toDto(poll));
        }

        result.sort((a, b) -> b.dueDate().compareTo(a.dueDate()));
        return result;
    }

    public List<ParticipationViewDto> viewAnswers(Long pollId, User creator) { // F-V1. Users who created and finished
        // a poll can view the answers given by each participant of the poll.

        Poll poll = serviceUtils.getOwnedPollOrThrow(pollId, creator);

        if (poll.getStatus() != PollStatus.FINISHED) {
            throw new PollNotFinishedException(pollId);
        }

        List<Participation> participations = participationRepository.findByPollId(pollId);

        List<ParticipationViewDto> result = new ArrayList<>();

        for (Participation p : participations) {
            List<AnswerViewDto> answers = new ArrayList<>();

            for (Answer a : p.getAnswers()) {
                answers.add(new AnswerViewDto(
                        a.getId(),
                        a.getText(),
                        a.getQuestion().getId()
                ));
            }

            result.add(new ParticipationViewDto(p.getParticipant().getUsername(), answers));
        }

        return result;
    }

    public AggregateResultsDto viewAggregateResults(Long pollId, User creator) { // F-V2. Users who created and finished
        // a poll can view aggregate results of boolean and numeric questions

        Poll poll = serviceUtils.getOwnedPollOrThrow(pollId, creator);

        if  (poll.getStatus() != PollStatus.FINISHED) {
            throw new PollNotFinishedException(pollId);
        }

        List<Participation> participations = participationRepository.findByPollId(pollId);

        if (participations.isEmpty()) {
            return new AggregateResultsDto(pollId, List.of());
        }

        List<QuestionResultDto> results = new ArrayList<>();

        Map<Long, List<Answer>> answersByQuestion = new HashMap<>();

        for (Participation p : participations) {
            for (Answer a : p.getAnswers()) {
                answersByQuestion
                        .computeIfAbsent(a.getQuestion().getId(), k -> new ArrayList<>())
                        .add(a);
            }
        }

        for (Question q : poll.getQuestions()) {

            if (q.getType() == QuestionType.TEXT) {
                continue;
            }

            List<Answer> answers = answersByQuestion.getOrDefault(q.getId(), List.of());

            int yes = 0;
            int no = 0;
            int sum = 0;
            int numericCount = 0;

            for (Answer a : answers) {
                String value = a.getText().trim().toLowerCase();

                if (q.getType() == QuestionType.BOOLEAN) {

                    if (value.equals("yes") || value.equals("true")) {
                        yes++;
                    } else if (value.equals("no") || value.equals("false")) {
                        no++;
                    } else {
                        throw new InvalidArgumentException("Invalid boolean answer: " + value);
                    }
                }

                if (q.getType() == QuestionType.NUMERIC) {
                    int number;

                    try {
                        number = ServiceUtils.mapToNumber(value);
                    } catch (InvalidArgumentException e) {
                        throw new InvalidArgumentException("Invalid numeric answer: " + value);
                    }
                    sum += number;
                    numericCount++;
                }
            }

            double avg = numericCount == 0 ? 0.0 : (double) sum / numericCount;

            results.add(new QuestionResultDto(
                    q.getId(),
                    q.getQuestion(),
                    q.getType(),
                    yes,
                    no,
                    avg
            ));
        }

        return new AggregateResultsDto(pollId, results);
    }

    public void deletePoll(Long pollId, User creator) { // F-C5. Creator of the polls can delete the poll
        Poll poll = serviceUtils.getOwnedPollOrThrow(pollId, creator);
        pollRepository.delete(poll);
    }

    public PollDetailDto getPoll(Long pollId, User user) {

        Poll poll = serviceUtils.getPollOrThrow(pollId);

        boolean isCreator = poll.getCreator().getId().equals(user.getId());

        boolean isInvited = poll.getInvitedUsers()
                .stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        if (!isCreator && !isInvited) {
            throw new UnauthorizedPollAccessException();
        }

        List<QuestionDto> questions = new ArrayList<>();

        for (Question q : poll.getQuestions()) {
            questions.add(new QuestionDto(
                    q.getId(),
                    q.getQuestion(),
                    q.getType()
            ));
        }

        return new PollDetailDto(
                poll.getId(),
                poll.getTitle(),
                poll.getDescription(),
                poll.getDueDate(),
                poll.getStatus(),
                questions
        );
    }

    public List<PollResponseDto> getMyPolls(User user) {
        List<PollResponseDto> result = new ArrayList<>();

        for (Poll poll : pollRepository.findByCreatorId(user.getId())) {
            result.add(toDto(poll));
        }

        return result;
    }
}
