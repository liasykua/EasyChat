package liasykua.easychat.commands;

import liasykua.easychat.EasyChat;
import liasykua.easychat.utils.ChatFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ClearChatCommand implements CommandExecutor {

    private final EasyChat plugin;

    public ClearChatCommand(EasyChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (!sender.hasPermission("easychat.clearchat")) {
            sender.sendMessage(ChatFormatter.msgComponent(plugin, "no-permission"));
            return true;
        }

        String clearerName = (sender instanceof Player p) ? p.getName() : "Console";
        Component blank = Component.text(" ");

        for (Player online : plugin.getServer().getOnlinePlayers()) {
            for (int i = 0; i < 100; i++) {
                online.sendMessage(blank);
            }
            online.sendMessage(
                    ChatFormatter.msgComponent(plugin, "chat-cleared", "{player}", clearerName)
            );
        }

        return true;
    }
}