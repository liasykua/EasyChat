package liasykua.easychat.commands;

import liasykua.easychat.EasyChat;
import liasykua.easychat.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class SpyCommand implements CommandExecutor {

    private final EasyChat plugin;

    public SpyCommand(EasyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("easychat.spy")) {
            player.sendMessage(ChatFormatter.msgComponent(plugin, "no-permission"));
            return true;
        }

        boolean enabled = plugin.getSpyManager().toggle(player.getUniqueId());
        player.sendMessage(ChatFormatter.msgComponent(plugin, enabled ? "spy-enabled" : "spy-disabled"));

        return true;
    }
}