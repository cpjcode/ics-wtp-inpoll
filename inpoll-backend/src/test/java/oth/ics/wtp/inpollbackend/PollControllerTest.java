package oth.ics.wtp.inpollbackend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import oth.ics.wtp.inpollbackend.controllers.ParticipationController;
import oth.ics.wtp.inpollbackend.controllers.PollController;
import oth.ics.wtp.inpollbackend.dtos.*;
import oth.ics.wtp.inpollbackend.entities.QuestionType;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PollControllerTest extends InPollControllerTestBase {

    @Autowired
    private PollController controller;

    @Autowired
    private ParticipationController participationController;

    @Test
    void testCreateUpdateFinishFlow() {

        authenticateAsCreator();
        // create
        CreatePollDto createDto = new CreatePollDto(
                "Test Poll",
                "desc",
                Instant.now().plusSeconds(3600),
                List.of()
        );

        var created = controller.createPoll(createDto);

        assertNotNull(created.pollId());
        Long pollId = created.pollId();

        // update
        UpdatePollDto updateDto = new UpdatePollDto(
                "Updated Title",
                "Updated Desc",
                Instant.now().plusSeconds(7200)
        );

        var updated = controller.updatePoll(pollId, updateDto);
        assertEquals("Updated Title", updated.title());

        // finish
        var finished = controller.finishPoll(pollId);
        assertEquals(pollId, finished.pollId());
    }

    @Test
    void testGetMyPolls() {

        authenticateAsCreator();

        controller.createPoll(new CreatePollDto(
                "MyPollTest",
                "desc",
                Instant.now().plusSeconds(1000),
                List.of()
        ));

        List<PollResponseDto> result = controller.getMyPolls();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        assertTrue(result.stream()
                .anyMatch(p -> p.title().equals("MyPollTest")));
    }

    @Test
    void testGetPollAllowedForCreator() {

        authenticateAsCreator();

        var created = controller.createPoll(
                new CreatePollDto("Poll", "desc", Instant.now().plusSeconds(1000), List.of())
        );

        var result = controller.getPoll(created.pollId());

        assertEquals("Poll", result.title());
    }

    @Test
    void testPendingPolls() {

        authenticateAsCreator();

        controller.createPoll(
                new CreatePollDto("Poll1", "desc", Instant.now().plusSeconds(1000), List.of())
        );

        authenticateAsUser1();

        List<PollResponseDto> pending = controller.getPendingPolls();

        assertNotNull(pending);
    }

    @Test
    void testAggregateResultShouldExecuteLogic() {

        authenticateAsCreator();
        PollResponseDto poll = controller.createPoll(new CreatePollDto(
                "P1",
                "desc",
                null,
                List.of(
                        new CreateQuestionDto("Q1", QuestionType.BOOLEAN),
                        new CreateQuestionDto("Q2", QuestionType.NUMERIC)
                )
        ));

        Long pollId = poll.pollId();

        controller.inviteToPoll(pollId, "u1");

        authenticateAsUser1();
        participationController.participateInPoll(pollId);

        var detail = controller.getPoll(pollId);

        Long q1 = detail.questions().get(0).id();
        Long q2 = detail.questions().get(1).id();

        participationController.submitParticipation(
                pollId,
                List.of(
                        new SubmitAnswerDto(q1, "true"),
                        new SubmitAnswerDto(q2, "14")
                )
        );

        authenticateAsCreator();
        controller.finishPoll(pollId);

        var aggregate = controller.getAggregate(pollId);

        assertNotNull(aggregate);
        assertFalse(aggregate.results().isEmpty());
    }

    @Test
    void testDeletePoll() {

        authenticateAsCreator();

        PollResponseDto poll = controller.createPoll(new CreatePollDto(
                "P1",
                "desc",
                null,
                List.of(new CreateQuestionDto("Q1", QuestionType.TEXT))
        ));

        Long pollId = poll.pollId();

        controller.deletePoll(pollId);

        var pending = controller.getPendingPolls();

        assertNotNull(pending);
    }

    @Test
    void testGetPendingPollsShouldReturnEmptyOrList() {

        authenticateAsUser1();

        var result = controller.getPendingPolls();

        assertNotNull(result);
    }

}
