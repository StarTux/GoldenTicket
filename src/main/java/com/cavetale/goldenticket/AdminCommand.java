package com.cavetale.goldenticket;

import com.cavetale.core.command.AbstractCommand;
import com.cavetale.core.command.CommandWarn;
import com.winthier.playercache.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class AdminCommand extends AbstractCommand<GoldenTicketPlugin> {
    protected AdminCommand(final GoldenTicketPlugin plugin) {
        super(plugin, "goldenticketadmin");
    }

    @Override
    protected void onEnable() {
        rootNode.addChild("add").arguments("<player>")
            .description("Add a player")
            .completers(PlayerCache.NAME_COMPLETER)
            .senderCaller(this::add);
        rootNode.addChild("save").denyTabCompletion()
            .description("Save to disk")
            .senderCaller(this::save);
        rootNode.addChild("reload").denyTabCompletion()
            .description("Reload from disk")
            .senderCaller(this::reload);
    }

    protected boolean add(CommandSender sender, String[] args) {
        if (args.length != 1) return false;
        PlayerCache player = PlayerCache.forArg(args[0]);
        if (player == null) throw new CommandWarn("Player not found: " + args[0]);
        plugin.save.members.put(player.uuid, player.getName());
        plugin.save();
        Player target = Bukkit.getPlayer(player.uuid);
        if (target != null) plugin.goldenTicketCommand.notify(target);
        sender.sendMessage(player.getName() + " added!");
        return true;
    }

    protected boolean save(CommandSender sender, String[] args) {
        if (args.length != 0) return false;
        plugin.save();
        sender.sendMessage("Saved to disk");
        return true;
    }

    protected boolean reload(CommandSender sender, String[] args) {
        if (args.length != 0) return false;
        plugin.load();
        sender.sendMessage("Reloaded from disk");
        return true;
    }
}
