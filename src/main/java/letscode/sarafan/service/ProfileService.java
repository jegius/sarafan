package letscode.sarafan.service;

import letscode.sarafan.domain.User;
import letscode.sarafan.domain.UserSubscription;
import letscode.sarafan.repositiories.UserDetailsRepo;
import letscode.sarafan.repositiories.UserSubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;
    private final UserSubscriptionRepo userSubscriptionRepo;

    @Autowired
    public ProfileService(
            UserDetailsRepo userDetailsRepo,
            UserSubscriptionRepo userSubscriptionRepo
    ) {
        this.userDetailsRepo = userDetailsRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
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

    public List<UserSubscription> getSubscribers(String channelId) {
        User channel = getProfile(channelId);
        return userSubscriptionRepo.findByChannel(channel);
    }

    public UserSubscription changeSubscriptionStatus(User channel, String subscriberId) {
        User subscriber = getProfile(subscriberId);
        UserSubscription userSubscription = userSubscriptionRepo.findByChannelAndSubscriber(channel, subscriber);
        userSubscription.setActive(!userSubscription.isActive());

        return userSubscriptionRepo.save(userSubscription);
    }
}
