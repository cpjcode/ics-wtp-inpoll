package oth.ics.wtp.inpollbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.inpollbackend.dtos.*;
import oth.ics.wtp.inpollbackend.entities.User;
import oth.ics.wtp.inpollbackend.services.PollService;
import oth.ics.wtp.inpollbackend.services.UserService;

import java.util.List;

@SecurityRequirement(name = "basicAuth")
@RestController
public class PollController {
    private final PollService pollService;
    private final UserService userService;

    @Autowired
    public PollController(PollService pollService, UserService userService) {
        this.pollService = pollService;
        this.userService = userService;
    }

    @GetMapping("/polls")
    public List<PollResponseDto> getMyPolls() {
        User user = userService.getCurrentUser();
        return pollService.getMyPolls(user);
    }

    @PostMapping("/polls")
    @ResponseStatus(HttpStatus.CREATED)
    public PollResponseDto createPoll(@RequestBody CreatePollDto dto) { // F-C1
        User creator = userService.getCurrentUser();
        return pollService.createPoll(dto, creator);
    }

    @PutMapping("/polls/{id}")
    public PollResponseDto updatePoll(@PathVariable Long id, @RequestBody UpdatePollDto dto) { // F-C4
        User creator = userService.getCurrentUser();
        return pollService.updatePoll(id, dto, creator);
    }

    @PostMapping("/polls/{id}/invite")
    public PollResponseDto inviteToPoll(@PathVariable Long id, @RequestParam String username) { // F-C2
        User creator = userService.getCurrentUser();
        return pollService.inviteToPoll(id, username, creator);
    }

    @PostMapping("/polls/{id}/finish")
    public PollResponseDto finishPoll(@PathVariable Long id) { // F-C3
        User creator = userService.getCurrentUser();
        return pollService.finishPoll(id, creator);
    }

    @GetMapping("/polls/{id}")
    public PollDetailDto getPoll(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        return pollService.getPoll(id, user);
    }

    @GetMapping("/polls/pending")
    public List<PollResponseDto> getPendingPolls() { // F-P1
        User user = userService.getCurrentUser();
        return pollService.getPendingPolls(user);
    }

    @GetMapping("/polls/{id}/answers")
    public List<ParticipationViewDto> getAnswers(@PathVariable Long id) { // F-V1
        User creator = userService.getCurrentUser();
        return pollService.viewAnswers(id, creator);
    }

    @GetMapping("/polls/{id}/aggregate")
    public AggregateResultsDto getAggregate(@PathVariable Long id) { // F-V2
        User creator = userService.getCurrentUser();
        return pollService.viewAggregateResults(id, creator);
    }

    @DeleteMapping("/polls/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoll(@PathVariable Long id) {
        User creator = userService.getCurrentUser();
        pollService.deletePoll(id, creator);
    }
}
