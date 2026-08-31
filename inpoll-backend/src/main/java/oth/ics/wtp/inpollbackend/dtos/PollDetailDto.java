package oth.ics.wtp.inpollbackend.dtos;

import oth.ics.wtp.inpollbackend.entities.PollStatus;

import java.time.Instant;
import java.util.List;

public record PollDetailDto(
        Long id,
        String title,
        String description,
        Instant dueDate,
        PollStatus pollStatus,
        List<QuestionDto> questions
) {}
