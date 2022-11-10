package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.Create;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/users/{userId}/events/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("{eventId}")
    public CommentDto addComment(@PathVariable long eventId,
                                 @PathVariable long userId,
                                 @RequestBody @Validated(Create.class) NewCommentDto newCommentDto) {
        log.info("Add comment {} to event {} from user {}", newCommentDto, eventId, userId);
        return commentService.addComment(newCommentDto, eventId, userId);
    }

    @GetMapping("{commentId}")
    public CommentDto getComment(@PathVariable long commentId, @PathVariable long userId) {
        log.info("Delete comment {} by user {}", commentId, userId);
        return commentService.getComment(commentId);
    }

    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable long userId, @PathVariable long commentId) {
        log.info("Delete comment {} by user {}", commentId, userId);
        commentService.deleteComment(commentId, userId);
    }

    @PatchMapping("{commentId}")
    public CommentDto changeComment(@PathVariable long userId,
                                    @RequestBody @Validated(Create.class) NewCommentDto commentDto,
                                    @PathVariable long commentId) {
        log.info("Change comment {} by user {}", commentId, userId);
        return commentService.changeComment(userId, commentId, commentDto);
    }
}
