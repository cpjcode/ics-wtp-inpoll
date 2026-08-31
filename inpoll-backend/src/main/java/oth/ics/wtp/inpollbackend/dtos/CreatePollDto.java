package oth.ics.wtp.inpollbackend.dtos;

import java.time.Instant;
import java.util.List;

public record CreatePollDto(String title, String description, Instant dueDate, List<CreateQuestionDto> questions) {}