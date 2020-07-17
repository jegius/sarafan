package letscode.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import letscode.sarafan.domain.Message;
import letscode.sarafan.domain.User;
import letscode.sarafan.domain.Views;
import letscode.sarafan.dto.MessagePageDto;
import letscode.sarafan.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("message")
public class MessageController {
    public static final int MESSAGES_PER_PAGE = 3;

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @JsonView({Views.FullMessage.class})
    public MessagePageDto list(
            @PageableDefault(size = MESSAGES_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return messageService.findAll(pageable);
    }

    @GetMapping("{id}")
    @JsonView({Views.FullMessage.class})
    public Optional<Message> getOne(@PathVariable("id") Long messageId) {
        return messageService.getOne(messageId);
    }

    @PostMapping
    public Message create(
            @RequestBody Message message,
            @AuthenticationPrincipal User user
    ) throws IOException {
        return messageService.createMssage(message, user);
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Long messageId,
            @RequestBody Message message
    ) throws IOException {
        return messageService.updateMessage(messageId, message);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long messageId) {
        messageService.deleteMessage(messageId);
    }
}
