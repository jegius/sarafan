package letscode.sarafan.service;

import letscode.sarafan.domain.User;
import letscode.sarafan.domain.UserSubscription;
import letscode.sarafan.repositiories.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;

    @Autowired
    public ProfileService(UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
    }

    public User getProfile(String userId) {
        return userDetailsRepo.findById(userId).get();
    }

    public User changeSubscription(String channelId, User subscriber) {
        User channel = getProfile(channelId);

        List<UserSubscription> subscriptions = channel
                .getSubscribers()
                .stream()
                .filter(subscription -> subscription.getSubscriber().equals(subscriber))
                .collect(Collectors.toList());

        if (subscriptions.isEmpty()) {
            UserSubscription subscription = new UserSubscription(channel, subscriber);
            channel.getSubscribers().add(subscription);
        } else {
            channel.getSubscribers().removeAll(subscriptions);
        }
        return userDetailsRepo.save(channel);
    }
}
