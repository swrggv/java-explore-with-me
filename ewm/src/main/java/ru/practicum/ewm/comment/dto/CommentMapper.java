package ru.practicum.ewm.comment.dto;

import ru.practicum.ewm.comment.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDto toCommentDtoFromComment(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setEventId(comment.getEvent().getId());
        dto.setUserId(comment.getUser().getId());
        return dto;
    }

    public static List<CommentDto> toListCommentDtoFromComment(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDtoFromComment)
                .collect(Collectors.toList());
    }
}
