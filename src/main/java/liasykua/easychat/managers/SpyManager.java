package liasykua.easychat.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpyManager {

    private final Set<UUID> spying = new HashSet<>();

    public boolean toggle(UUID uuid) {
        if (spying.contains(uuid)) {
            spying.remove(uuid);
            return false;
        } else {
            spying.add(uuid);
            return true;
        }
    }

    public boolean isSpy(UUID uuid) {
        return spying.contains(uuid);
    }
}