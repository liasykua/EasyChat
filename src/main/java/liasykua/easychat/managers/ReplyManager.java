package liasykua.easychat.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplyManager {

    private final Map<UUID, UUID> lastSender = new HashMap<>();

    public void setLastSender(UUID recipient, UUID sender) {
        lastSender.put(recipient, sender);
    }

    public UUID getLastSender(UUID recipient) {
        return lastSender.get(recipient);
    }
}