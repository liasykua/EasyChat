package liasykua.easychat.commands;

import liasykua.easychat.EasyChat;
import liasykua.easychat.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class IgnoreCommand implements CommandExecutor {

    private final EasyChat plugin;

    public IgnoreCommand(EasyChat plugin) {
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
            player.sendMessage(ChatFormatter.msgComponent(plugin, "ignore-usage"));
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "player-not-found"));
            return true;
        }

        boolean nowIgnoring = plugin.getIgnoreManager().toggle(player.getUniqueId(), target.getUniqueId());

        if (nowIgnoring) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "ignore-added", "{player}", target.getName()));
        } else {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "ignore-removed", "{player}", target.getName()));
        }

        return true;
    }
}