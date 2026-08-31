package oth.ics.wtp.inpollbackend.dtos;

import java.time.Instant;

public record UpdatePollDto(String title, String description, Instant dueDate) {}