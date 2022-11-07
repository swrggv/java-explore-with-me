package ru.practicum.ewm.comment;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

public interface CommentService {
    CommentDto addComment(NewCommentDto newCommentDto, long eventId, long userId);

    CommentDto getComment(long commentId);

    void deleteComment(long commentId, long userId);

    CommentDto changeComment(long userId, long commentId, NewCommentDto commentDto);
}
