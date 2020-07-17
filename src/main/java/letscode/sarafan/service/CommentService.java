package letscode.sarafan.service;

import letscode.sarafan.domain.Comment;
import letscode.sarafan.domain.User;
import letscode.sarafan.domain.Views;
import letscode.sarafan.dto.EventType;
import letscode.sarafan.dto.ObjectType;
import letscode.sarafan.repositiories.CommentRepository;
import letscode.sarafan.utils.WsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BiConsumer<EventType, Comment> wsSender;

    @Autowired
    public CommentService(CommentRepository commentRepository, WsSender sender) {

        this.wsSender = sender.getSender(ObjectType.COMMENT, Views.FullComment.class);
        this.commentRepository = commentRepository;
    }

    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        Comment commentFromDb = this.commentRepository.save(comment);
        wsSender.accept(EventType.CREATE, commentFromDb);
        return commentFromDb;
    }
}
