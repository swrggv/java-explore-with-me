package ru.practicum.ewm.comment.dto;

import org.mapstruct.Mapper;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentDto toCommentDto(Comment comment);
}
