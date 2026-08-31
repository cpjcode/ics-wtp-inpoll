package oth.ics.wtp.inpollbackend.dtos;

import oth.ics.wtp.inpollbackend.entities.PollStatus;

import java.time.Instant;

public record PollDto(Long id, String title, String description, Instant dueDate, PollStatus pollStatus) {}
