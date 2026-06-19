package liasykua.easychat.managers;

import liasykua.easychat.EasyChat;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IgnoreManager {

    private final EasyChat plugin;
    private final File dataFile;
    private final Map<UUID, Set<UUID>> ignoreMap = new HashMap<>();

    public IgnoreManager(EasyChat plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "ignore_data.yml");
        load();
    }

    private void load() {
        if (!dataFile.exists()) return;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (String key : config.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            List<String> list = config.getStringList(key);
            Set<UUID> ignored = new HashSet<>();
            for (String s : list) ignored.add(UUID.fromString(s));
            ignoreMap.put(uuid, ignored);
        }
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Set<UUID>> entry : ignoreMap.entrySet()) {
            List<String> list = new ArrayList<>();
            for (UUID u : entry.getValue()) list.add(u.toString());
            config.set(entry.getKey().toString(), list);
        }
        try {
            config.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save ignore data: " + e.getMessage());
        }
    }

    public boolean toggle(UUID player, UUID target) {
        Set<UUID> set = ignoreMap.computeIfAbsent(player, k -> new HashSet<>());
        if (set.contains(target)) {
            set.remove(target);
            save();
            return false;
        } else {
            set.add(target);
            save();
            return true;
        }
    }

    public boolean isIgnoring(UUID player, UUID target) {
        Set<UUID> set = ignoreMap.get(player);
        return set != null && set.contains(target);
    }
}