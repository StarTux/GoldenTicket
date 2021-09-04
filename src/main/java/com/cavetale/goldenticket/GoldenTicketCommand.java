package com.cavetale.goldenticket;

import com.cavetale.core.command.AbstractCommand;
import com.winthier.chat.ChatPlugin;
import java.util.Date;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public final class GoldenTicketCommand extends AbstractCommand<GoldenTicketPlugin> {
    protected GoldenTicketCommand(final GoldenTicketPlugin plugin) {
        super(plugin, "goldenticket");
    }

    @Override
    protected void onEnable() {
        rootNode.playerCaller(this::info);
        rootNode.addChild("accept").denyTabCompletion()
            .hidden(true)
            .playerCaller(this::accept);
        rootNode.addChild("decline").denyTabCompletion()
            .hidden(true)
            .playerCaller(this::decline);
    }

    protected boolean info(Player player, String[] args) {
        if (args.length != 0) return true;
        if (!plugin.save.members.containsKey(player.getUniqueId())) return true;
        player.sendMessage(TextComponent.ofChildren(new Component[] {
                    Component.newline(),
                    Component.text("You are cordially invited to join the ", NamedTextColor.WHITE),
                    Component.text("Builder Rank", NamedTextColor.BLUE),
                    Component.text("!", NamedTextColor.WHITE),
                    Component.newline(),
                    Component.text("Click here to ", NamedTextColor.WHITE),
                    (Component.text()
                     .content("[Accept]")
                     .color(NamedTextColor.GREEN)
                     .hoverEvent(HoverEvent.showText(Component.text("Accept", NamedTextColor.GREEN)))
                     .clickEvent(ClickEvent.runCommand("/goldenticket accept"))
                     .build()),
                    Component.text(" or ", NamedTextColor.WHITE),
                    (Component.text()
                     .content("[Decline]")
                     .color(NamedTextColor.AQUA)
                     .hoverEvent(HoverEvent.showText(Component.text("Decline", NamedTextColor.GOLD)))
                     .clickEvent(ClickEvent.runCommand("/goldenticket decline"))
                     .build()),
                    Component.newline()
                }));
        return true;
    }

    protected boolean accept(Player player, String[] args) {
        if (args.length != 0) return true;
        if (null == plugin.save.members.remove(player.getUniqueId())) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 0.5f);
            return true;
        }
        plugin.save.logs.add(player.getName() + " accepted " + new Date());
        plugin.save();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "perm player " + player.getName() + " addgroup builder");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "titles set " + player.getName() + " Builder");
        player.sendMessage(Component.text("Welcome to the team!.", NamedTextColor.GREEN));
        ChatPlugin.getInstance().announce("b", Component.text(player.getName() + " accepted their invitation to Builder!", NamedTextColor.GOLD));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1.0f, 2.0f);
        return true;
    }

    protected boolean decline(Player player, String[] args) {
        if (args.length != 0) return true;
        if (null == plugin.save.members.remove(player.getUniqueId())) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 0.5f);
            return true;
        }
        plugin.save.logs.add(player.getName() + " declined " + new Date());
        plugin.save();
        player.sendMessage(Component.text("We're sorry that you decline. Maybe next time.", NamedTextColor.RED));
        ChatPlugin.getInstance().announce("a", Component.text(player.getName() + " declined their invitation to Builder!", NamedTextColor.YELLOW));
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 0.5f);
        return true;
    }

    protected void notify(Player player) {
        player.sendMessage(TextComponent.ofChildren(new Component[] {
                    Component.newline(),
                    Component.text("You received a ", NamedTextColor.WHITE),
                    (Component.text()
                     .content("[Golden Ticket]")
                     .color(NamedTextColor.GOLD)
                     .hoverEvent(HoverEvent.showText(Component.text("Golden Ticket", NamedTextColor.GOLD)))
                     .clickEvent(ClickEvent.runCommand("/goldenticket"))
                     .build()),
                    Component.text(". Click it to learn more!", NamedTextColor.WHITE),
                    Component.newline()
                }));
    }
}
