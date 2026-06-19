package liasykua.easychat.commands;

import liasykua.easychat.EasyChat;
import liasykua.easychat.utils.ChatFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class ReplyCommand implements CommandExecutor {

    private final EasyChat plugin;

    public ReplyCommand(EasyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("easychat.chat.personal")) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "no-permission"));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "reply-usage"));
            return true;
        }

        UUID targetUuid = plugin.getReplyManager().getLastSender(player.getUniqueId());
        if (targetUuid == null) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "no-reply-target"));
            return true;
        }

        Player target = plugin.getServer().getPlayer(targetUuid);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "player-not-found"));
            return true;
        }

        if (plugin.getIgnoreManager().isIgnoring(target.getUniqueId(), player.getUniqueId())) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "ignored-by-player"));
            return true;
        }

        if (plugin.getIgnoreManager().isIgnoring(player.getUniqueId(), target.getUniqueId())) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "you-are-ignored"));
            return true;
        }

        String message = String.join(" ", args);
        Component formatted = ChatFormatter.formatPersonal(plugin, player, target, message);

        player.sendMessage(formatted);
        target.sendMessage(formatted);

        plugin.getReplyManager().setLastSender(target.getUniqueId(), player.getUniqueId());
        plugin.getReplyManager().setLastSender(player.getUniqueId(), target.getUniqueId());

        Component spyFormatted = ChatFormatter.formatSocialSpy(plugin, formatted);
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (!online.equals(player) && !online.equals(target)
                    && plugin.getSpyManager().isSpy(online.getUniqueId())) {
                online.sendMessage(spyFormatted);
            }
        }

        return true;
    }
}