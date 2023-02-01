package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentMapper;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.ModelNotFoundException;
import ru.practicum.ewm.exception.NoRootException;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper mapper;

    @Override
    @Transactional
    public CommentDto addComment(NewCommentDto newCommentDto, long eventId, long userId) {
        User user = fromOptionalToUser(userId);
        Event event = fromOptionalToEvent(eventId);
        Comment comment = new Comment(newCommentDto.getText(), user, event);
        comment = commentRepository.save(comment);
        return mapper.toCommentDto(comment);
    }

    @Override
    public CommentDto getComment(long commentId) {
        return mapper.toCommentDto(fromOptionalToComment(commentId));
    }

    @Override
    @Transactional
    public void deleteComment(long commentId, long userId) {
        Comment comment = fromOptionalToComment(commentId);
        checkOwner(comment, userId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto changeComment(long userId, long commentId, NewCommentDto commentDto) {
        Comment comment = fromOptionalToComment(commentId);
        checkOwner(comment, userId);
        comment.setText(commentDto.getText());
        comment = commentRepository.save(comment);
        return mapper.toCommentDto(comment);
    }

    @Override
    public void deleteCommentAdmin(long comId) {
        commentRepository.deleteById(comId);
    }

    private void checkOwner(Comment comment, long userId) {
        if (comment.getUser().getId() != userId) {
            throw new NoRootException("No root",
                    String.format("User %d is not the owner of comment %d", userId, comment.getId()));
        }
    }

    private Event fromOptionalToEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new ModelNotFoundException("Event not found",
                        String.format("Event %d not found", eventId))
        );
    }

    private User fromOptionalToUser(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ModelNotFoundException("User not found",
                        String.format("User %d not found", userId))
        );
    }

    private Comment fromOptionalToComment(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new ModelNotFoundException("Comment not found",
                        String.format("Comment %d not found", commentId))
        );
    }
}
