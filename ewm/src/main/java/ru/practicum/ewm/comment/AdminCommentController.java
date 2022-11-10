package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("{comId}")
    public void deleteComment(@PathVariable long comId) {
        log.info("Delete comment {}", comId);
        commentService.deleteCommentAdmin(comId);
    }
}
