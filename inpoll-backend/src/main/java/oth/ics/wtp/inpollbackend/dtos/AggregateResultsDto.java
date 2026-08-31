package oth.ics.wtp.inpollbackend.dtos;

import java.util.List;

public record AggregateResultsDto(Long pollId, List<QuestionResultDto> results) {}
