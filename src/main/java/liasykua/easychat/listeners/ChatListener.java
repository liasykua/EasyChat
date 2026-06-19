package liasykua.easychat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import liasykua.easychat.EasyChat;
import liasykua.easychat.utils.ChatFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final EasyChat plugin;

    public ChatListener(EasyChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        String globalSymbol = plugin.getConfig().getString("chats.global.symbol", "!");
        boolean isGlobal = message.startsWith(globalSymbol);

        if (isGlobal) {
            if (!player.hasPermission("easychat.chat.global")) {
                player.sendMessage(ChatFormatter.msgComponent(plugin, "no-global-permission"));
                return;
            }
            String actualMessage = message.substring(globalSymbol.length()).trim();
            sendGlobal(player, actualMessage);
        } else {
            if (!player.hasPermission("easychat.chat.local")) {
                player.sendMessage(ChatFormatter.msgComponent(plugin, "no-local-permission"));
                return;
            }
            sendLocal(player, message);
        }
    }

    private void sendLocal(Player sender, String message) {
        Component formatted = ChatFormatter.formatLocal(plugin, sender, message);
        double radius = parseRadius(plugin.getConfig().getString("chats.local.radius", "100"));

        Location origin = sender.getLocation();
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (online.getWorld().equals(origin.getWorld())) {
                double dist = online.getLocation().distance(origin);
                if (radius < 0 || dist <= radius) {
                    online.sendMessage(formatted);
                }
            }
        }
    }

    private void sendGlobal(Player sender, String message) {
        Component formatted = ChatFormatter.formatGlobal(plugin, sender, message);
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            online.sendMessage(formatted);
        }
    }

    private double parseRadius(String raw) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return 100;
        }
    }
}