package oth.ics.wtp.inpollbackend.dtos;

import oth.ics.wtp.inpollbackend.entities.QuestionType;

public record QuestionDto(Long id, String question, QuestionType questionType) {}
