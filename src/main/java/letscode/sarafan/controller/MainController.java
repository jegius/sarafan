package letscode.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import letscode.sarafan.domain.User;
import letscode.sarafan.domain.Views;
import letscode.sarafan.dto.MessagePageDto;
import letscode.sarafan.repositiories.UserDetailsRepo;
import letscode.sarafan.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

import static letscode.sarafan.controller.MessageController.MESSAGES_PER_PAGE;

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageService messageService;
    private final ObjectWriter messageWriter;
    private final ObjectWriter profileWriter;
    private final UserDetailsRepo userDetailsRepo;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    public MainController(MessageService mess, ObjectMapper mapper, UserDetailsRepo userDetailsRepo) {
        this.messageService = mess;
        ObjectMapper objectMapper = mapper
                .setConfig(mapper.getSerializationConfig());
        this.messageWriter = objectMapper
                .writerWithView(Views.FullMessage.class);
        this.profileWriter = objectMapper
                .writerWithView(Views.FullProfile.class);
        this.userDetailsRepo = userDetailsRepo;
    }

    @GetMapping
    @JsonView({Views.FullMessage.class})
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            User userFromDb = userDetailsRepo.findById(user.getId()).get();
            String serializedProfile = profileWriter.writeValueAsString(userFromDb);

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, MESSAGES_PER_PAGE, sort);
            MessagePageDto messagePageDto = messageService.findForUser(pageRequest, user);

            String messages = messageWriter.writeValueAsString(messagePageDto.getMessages());

            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());

            model.addAttribute("profile", serializedProfile);
            model.addAttribute("messages", messages);
        } else {
            model.addAttribute("messages", "[]");
            model.addAttribute("profile", "null");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }
}
