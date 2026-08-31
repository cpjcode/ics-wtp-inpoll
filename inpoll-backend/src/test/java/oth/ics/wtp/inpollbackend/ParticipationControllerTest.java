package oth.ics.wtp.inpollbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import oth.ics.wtp.inpollbackend.controllers.ParticipationController;
import oth.ics.wtp.inpollbackend.controllers.PollController;
import oth.ics.wtp.inpollbackend.dtos.*;
import oth.ics.wtp.inpollbackend.entities.QuestionType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipationControllerTest extends InPollControllerTestBase {

    @Autowired
    private ParticipationController participationController;

    @Autowired
    private PollController pollController;

    @Test
    void testParticipateAndSubmitFlow() {

        // creating the poll
        authenticateAsCreator();

        PollResponseDto poll = pollController.createPoll(new CreatePollDto(
                "P1",
                "desc",
                null,
                List.of(new CreateQuestionDto("Q1", QuestionType.TEXT),
                        new CreateQuestionDto("Q2", QuestionType.TEXT))
        ));

        Long pollId = poll.pollId();

        // invite user
        pollController.inviteToPoll(pollId, "u1");

        // participate
        authenticateAsUser1();

        ParticipationDto participation = participationController.participateInPoll(pollId);

        assertNotNull(participation);
        assertEquals(pollId, participation.pollId());

        // getting poll details for question ids
        PollDetailDto detail = pollController.getPoll(pollId);

        Long q1Id = detail.questions().get(0).id();
        Long q2Id = detail.questions().get(1).id();

        // submit answers
        ParticipationDto submitted = participationController.submitParticipation(
                pollId,
                List.of(
                        new SubmitAnswerDto(q1Id, "A1"),
                        new SubmitAnswerDto(q2Id, "A2")
                )
        );

        assertNotNull(submitted);
        assertEquals(pollId, submitted.pollId());
    }

    @Test
    void testViewAnswersFlow() {

        // create poll
        authenticateAsCreator();

        PollResponseDto poll = pollController.createPoll(new CreatePollDto(
                "P2",
                "desc",
                null,
                List.of(new CreateQuestionDto("Q1", QuestionType.TEXT))
        ));

        Long pollId = poll.pollId();

        // get poll details for question ids
        PollDetailDto detail = pollController.getPoll(pollId);
        Long questionId = detail.questions().get(0).id();

        // invite
        pollController.inviteToPoll(pollId, "u1");

        // participate
        authenticateAsUser1();
        participationController.participateInPoll(pollId);

        // submit answers
        participationController.submitParticipation(
                pollId,
                List.of(new SubmitAnswerDto(questionId, "X"))
        );

        // finish poll
        authenticateAsCreator();
        pollController.finishPoll(pollId);

        // view answers
        List<ParticipationViewDto> result = pollController.getAnswers(pollId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testSubmitParticipationInvalidAnswersShouldFail() {

        authenticateAsCreator();
        PollResponseDto poll = pollController.createPoll(new CreatePollDto(
                "P1",
                "desc",
                null,
                List.of(
                        new CreateQuestionDto("Q1", QuestionType.TEXT),
                        new CreateQuestionDto("Q2", QuestionType.TEXT)
                )
        ));

        Long pollId = poll.pollId();

        pollController.inviteToPoll(pollId, "u1");

        authenticateAsUser1();
        participationController.participateInPoll(pollId);

        var detail = pollController.getPoll(pollId);

        Long q1Id = detail.questions().get(0).id();

        // missing second answer
        assertThrows(Exception.class, () -> {
            participationController.submitParticipation(
                    pollId,
                    List.of(new SubmitAnswerDto(q1Id, "only one answer"))
            );
        });
    }

    @Test
    void testViewAnswers_shouldWorkOnlyWhenFinished() {

        authenticateAsCreator();

        PollResponseDto poll = pollController.createPoll(new CreatePollDto(
                "P2",
                "desc",
                null,
                List.of(new CreateQuestionDto("Q1", QuestionType.TEXT))
        ));

        Long pollId = poll.pollId();

        authenticateAsCreator();

        pollController.inviteToPoll(pollId, "u1");

        authenticateAsUser1();
        participationController.participateInPoll(pollId);

        var detail = pollController.getPoll(pollId);

        Long qId = detail.questions().get(0).id();

        participationController.submitParticipation(
                pollId,
                List.of(new SubmitAnswerDto(qId, "X"))
        );

        authenticateAsCreator();
        pollController.finishPoll(pollId);

        var result = pollController.getAnswers(pollId);

        assertNotNull(result);
    }
}