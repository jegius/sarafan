package letscode.sarafan.service;

import letscode.sarafan.domain.Message;
import letscode.sarafan.domain.User;
import letscode.sarafan.domain.UserSubscription;
import letscode.sarafan.domain.Views;
import letscode.sarafan.dto.EventType;
import letscode.sarafan.dto.MessagePageDto;
import letscode.sarafan.dto.MetaDto;
import letscode.sarafan.dto.ObjectType;
import letscode.sarafan.repositiories.MessageRepository;
import letscode.sarafan.repositiories.UserSubscriptionRepo;
import letscode.sarafan.utils.WsSender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
    private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";

    private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);

    private final MessageRepository messageRepository;
    private final UserSubscriptionRepo userSubscriptionRepo;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            WsSender wsSender,
            UserSubscriptionRepo userSubscriptionRepo
    ) {
        this.messageRepository = messageRepository;
        this.userSubscriptionRepo = userSubscriptionRepo;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }


    public void deleteMessage(Long messageId) {
        Message messageFromDb = messageRepository.findById(messageId).get();
        wsSender.accept(EventType.REMOVE, messageFromDb);
        messageRepository.deleteById(messageId);
    }

    public Message updateMessage(Long messageId, Message message) throws IOException {

        Message messageFromDb = messageRepository.findById(messageId).get();
        BeanUtils.copyProperties(message, messageFromDb, "id");
        fillMeta(messageFromDb);
        Message updatedMessage = messageRepository.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);
        return updatedMessage;
    }

    private void fillMeta(Message message) throws IOException {
        String text = message.getText();

        Matcher matcher = URL_REGEX.matcher(text);

        if (matcher.find()) {
            String url = text.substring(matcher.start(), matcher.end());

            matcher = IMG_REGEX.matcher(url);
            message.setLink(url);

            if (matcher.find()) {
                message.setLinkCover(url);
            } else if (!url.contains("youtu")) {
                MetaDto metaDto = getMeta(url);

                message.setLinkCover(metaDto.getCover());
                message.setLinkTitle(metaDto.getTitle());
                message.setLinkDescription(metaDto.getDescription());
            }
        }
    }

    private MetaDto getMeta(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Elements title = document.select("meta[name$=title],meta[property$=title]");
        Elements description = document.select("meta[name$=description],meta[property$=description]");
        Elements cover = document.select("meta[name$=image],meta[property$=image]");
        return new MetaDto(
                getContent(title.first()),
                getContent(description.first()),
                getContent(cover.first())
        );
    }

    private String getContent(Element element) {
        return element == null ?
                "" :
                element.attr("content");
    }

    public Message createMssage(Message message, User user) throws IOException {
        message.setCreationDate(LocalDateTime.now());
        fillMeta(message);
        message.setAuthor(user);
        Message updatedMessage = messageRepository.save(message);

        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }

    public Optional<Message> getOne(Long messageId) {
        return messageRepository.findById(messageId);
    }

    public MessagePageDto findForUser(Pageable pageable, User user) {
        List<User> channels = userSubscriptionRepo.findBySubscriber(user)
                .stream()
                .map(UserSubscription::getChannel)
                .collect(Collectors.toList());

        channels.add(user);

        Page<Message> page = messageRepository.findByAuthorIn(channels, pageable);
        return new MessagePageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
    }
}
