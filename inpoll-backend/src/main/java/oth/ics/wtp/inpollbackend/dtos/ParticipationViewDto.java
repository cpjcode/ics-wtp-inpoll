package oth.ics.wtp.inpollbackend.dtos;

import java.util.List;

public record ParticipationViewDto(String username, List<AnswerViewDto> answers) {}