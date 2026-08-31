package oth.ics.wtp.inpollbackend.dtos;

import oth.ics.wtp.inpollbackend.entities.QuestionType;

public record CreateQuestionDto(String question, QuestionType type) {}
