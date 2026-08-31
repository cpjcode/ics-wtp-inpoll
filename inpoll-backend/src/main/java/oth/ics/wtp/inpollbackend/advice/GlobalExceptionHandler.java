package oth.ics.wtp.inpollbackend.advice;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import oth.ics.wtp.inpollbackend.dtos.ErrorDto;
import oth.ics.wtp.inpollbackend.exceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorDto handleAndLog(Exception e) { // Util method.
        logger.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleUserNotFound(UserNotFoundException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleUserAlreadyExists(UserAlreadyExistsException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(PollNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlePollNotFound(PollNotFoundException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(PollAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlePollAlreadyExists(PollAlreadyExistsException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(PollNotFinishedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handlePollNotFinished(PollNotFinishedException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(PollAlreadyFinishedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlePollAlreadyFinished(PollAlreadyFinishedException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleUnauthorizedUser(UnauthorizedUserException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(UnauthorizedPollAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handleUnauthorizedPollAccess(UnauthorizedPollAccessException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(ParticipationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleParticipationNotFound(ParticipationNotFoundException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(AlreadyParticipatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleAlreadyParticipated(AlreadyParticipatedException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(SelfInviteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleSelfInvite(SelfInviteException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(UnansweredQuestionsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleUnansweredQuestions(UnansweredQuestionsException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleInvalidArgument(InvalidArgumentException e) {
        return handleAndLog(e);
    }

    @ExceptionHandler(InvalidAnswerCountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleInvalidAnswerCount(InvalidAnswerCountException e) { return handleAndLog(e); }

    @ExceptionHandler(UninvitedUserException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handleUninvitedUser(UninvitedUserException e) { return handleAndLog(e); }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto log(Exception e) {
        logger.error("{}\n{}", ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));
        return new ErrorDto("Internal server error");
    }

}
