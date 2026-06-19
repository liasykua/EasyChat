package liasykua.easychat.commands;

import liasykua.easychat.EasyChat;
import liasykua.easychat.utils.ChatFormatter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

public class ReloadCommand implements CommandExecutor {

    private final EasyChat plugin;

    public ReloadCommand(EasyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!sender.hasPermission("easychat.reload")) {
            sender.sendMessage(ChatFormatter.msgComponent(plugin, "no-permission"));
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatFormatter.msgComponent(plugin, "plugin-reloaded"));
        return true;
    }
}