package oth.ics.wtp.inpollbackend.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.inpollbackend.dtos.ParticipationDto;
import oth.ics.wtp.inpollbackend.dtos.SubmitAnswerDto;
import oth.ics.wtp.inpollbackend.entities.User;
import oth.ics.wtp.inpollbackend.services.ParticipationService;
import oth.ics.wtp.inpollbackend.services.UserService;

import java.util.List;

@SecurityRequirement(name = "basicAuth")
@RestController
public class ParticipationController {

    private final ParticipationService participationService;
    private final UserService userService;

    @Autowired
    public ParticipationController(ParticipationService participationService, UserService userService) {
        this.participationService = participationService;
        this.userService = userService;
    }

    @PostMapping("/polls/{pollId}/participate")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationDto participateInPoll( // F-P2
            @PathVariable Long pollId
    ) {
        User user = userService.getCurrentUser();

        return participationService.participateInPoll(
                pollId,
                user
        );
    }

    @PostMapping("/polls/{pollId}/submit")
    public ParticipationDto submitParticipation(
            @PathVariable Long pollId,
            @RequestBody List<SubmitAnswerDto> answers
    ) {
        User user = userService.getCurrentUser();

        return participationService.submitParticipation(
                pollId,
                user,
                answers
        );
    }

}
