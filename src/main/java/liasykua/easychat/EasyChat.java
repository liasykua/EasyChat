package liasykua.easychat;

import liasykua.easychat.commands.*;
import liasykua.easychat.commands.*;
import liasykua.easychat.listeners.ChatListener;
import liasykua.easychat.managers.IgnoreManager;
import liasykua.easychat.managers.SpyManager;
import liasykua.easychat.managers.ReplyManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class EasyChat extends JavaPlugin {

    private LuckPerms luckPerms;
    private IgnoreManager ignoreManager;
    private SpyManager spyManager;
    private ReplyManager replyManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        RegisteredServiceProvider<LuckPerms> provider =
                getServer().getServicesManager().getRegistration(LuckPerms.class);

        if (provider == null) {
            getLogger().severe("LuckPerms не знайдено! EasyChat вимкнений!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        luckPerms = provider.getProvider();
        ignoreManager = new IgnoreManager(this);
        spyManager = new SpyManager();
        replyManager = new ReplyManager();

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);

        Objects.requireNonNull(getCommand("msg")).setExecutor(new MsgCommand(this));
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand(this));
        Objects.requireNonNull(getCommand("ignore")).setExecutor(new IgnoreCommand(this));
        Objects.requireNonNull(getCommand("spy")).setExecutor(new SpyCommand(this));
        Objects.requireNonNull(getCommand("clearchat")).setExecutor(new ClearChatCommand(this));
        Objects.requireNonNull(getCommand("easychatreload")).setExecutor(new ReloadCommand(this));

    }

    @Override
    public void onDisable() {
        if (ignoreManager != null) {
            ignoreManager.save();
        }
    }

    public LuckPerms getLuckPerms() { return luckPerms; }
    public IgnoreManager getIgnoreManager() { return ignoreManager; }
    public SpyManager getSpyManager() { return spyManager; }
    public ReplyManager getReplyManager() { return replyManager; }
}