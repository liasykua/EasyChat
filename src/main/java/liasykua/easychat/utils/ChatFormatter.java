package liasykua.easychat.utils;

import liasykua.easychat.EasyChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

public class ChatFormatter {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    public static Component formatLocal(EasyChat plugin, Player player, String message) {
        String format = plugin.getConfig().getString("chats.local.format", "{player}: {message}");
        return applyPlayerPlaceholders(plugin, player, format, message);
    }

    public static Component formatGlobal(EasyChat plugin, Player player, String message) {
        String format = plugin.getConfig().getString("chats.global.format", "{player}: {message}");
        return applyPlayerPlaceholders(plugin, player, format, message);
    }

    public static Component formatPersonal(EasyChat plugin, Player sender, Player recipient, String message) {
        String format = plugin.getConfig().getString("chats.personal.format", "{sender} -> {recipient}: {message}");
        format = format
                .replace("{sender}", sender.getName())
                .replace("{recipient}", recipient.getName())
                .replace("{message}", message);
        return LEGACY.deserialize(format);
    }

    public static Component formatSocialSpy(EasyChat plugin, Component original) {
        String spyFormat = plugin.getConfig().getString("chats.social-spy.format", "&e[S] {chat}");
        String prefix = spyFormat.replace("{chat}", "");
        Component prefixComponent = LEGACY.deserialize(prefix);
        return prefixComponent.append(original);
    }

    private static Component applyPlayerPlaceholders(EasyChat plugin, Player player, String format, String message) {
        User user = plugin.getLuckPerms().getUserManager().getUser(player.getUniqueId());
        String prefix = "";
        String suffix = "";
        if (user != null) {
            prefix = user.getCachedData().getMetaData().getPrefix() != null
                    ? user.getCachedData().getMetaData().getPrefix() : "";
            suffix = user.getCachedData().getMetaData().getSuffix() != null
                    ? user.getCachedData().getMetaData().getSuffix() : "";
        }
        format = format
                .replace("{prefix}", prefix)
                .replace("{suffix}", suffix)
                .replace("{player}", player.getName())
                .replace("{message}", message);
        return LEGACY.deserialize(format);
    }

    public static String msg(EasyChat plugin, String key) {
        return plugin.getConfig().getString("messages." + key, "&cMessage not found: " + key);
    }

    public static Component msgComponent(EasyChat plugin, String key) {
        return LEGACY.deserialize(msg(plugin, key));
    }

    public static Component msgComponent(EasyChat plugin, String key, String... replacements) {
        String raw = msg(plugin, key);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            raw = raw.replace(replacements[i], replacements[i + 1]);
        }
        return LEGACY.deserialize(raw);
    }
}