package oth.ics.wtp.inpollbackend.dtos;

import oth.ics.wtp.inpollbackend.entities.PollStatus;

import java.time.Instant;

public record PollResponseDto(Long pollId, String title, Instant dueDate, long questionCount, String username, PollStatus pollStatus) {}
