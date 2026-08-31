package oth.ics.wtp.inpollbackend.dtos;

import oth.ics.wtp.inpollbackend.entities.QuestionType;

public record QuestionResultDto(
        Long questionId,
        String question,
        QuestionType questionType,
        int yesCount,
        int noCount,
        double averageNumeric
) {}
