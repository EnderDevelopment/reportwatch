package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;

public final class Main extends JavaPlugin implements CommandExecutor {
    private static final String RECEIVE_PERMISSION = "report.receive";

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("report")).setExecutor(this);
        Objects.requireNonNull(getCommand("report-teleport-normal")).setExecutor(this);
        Objects.requireNonNull(getCommand("report-teleport-spec")).setExecutor(this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return switch (command.getName()) {
            case "report" -> handleReport(sender, args);
            case "report-teleport-normal" -> handleTeleport(sender, args, false);
            case "report-teleport-spec" -> handleTeleport(sender, args, true);
            default -> false;
        };
    }

    private boolean handleReport(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /report [player] [reason]", NamedTextColor.RED));
            return true;
        }
        if (sender instanceof Player player && player.isOp()) {
            sender.sendMessage(Component.text("Operators cannot submit reports.", NamedTextColor.RED));
            return true;
        }

        String targetName = args[0];
        if (sender instanceof Player player && player.getName().equalsIgnoreCase(targetName)) {
            sender.sendMessage(Component.text("You cannot report yourself.", NamedTextColor.RED));
            return true;
        }

        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Component reportHeader = Component.text("[Report] NEW REPORT!");
        Component reporter = Component.text("Reporter: " + sender.getName());
        Component target = Component.text("Target: " + targetName);
        Component reportReason = Component.text("Reason: " + reason);
        Component teleport = Component.text("[TELEPORT]")
                .color(NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/report-teleport-normal " + targetName));
        Component spectate = Component.text("[SPECTATE]")
                .color(NamedTextColor.YELLOW)
                .clickEvent(ClickEvent.runCommand("/report-teleport-spec " + targetName));
        Component actions = teleport.append(spectate);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(RECEIVE_PERMISSION)) {
                onlinePlayer.sendMessage(reportHeader);
                onlinePlayer.sendMessage(reporter);
                onlinePlayer.sendMessage(target);
                onlinePlayer.sendMessage(reportReason);
                onlinePlayer.sendMessage(actions);
            }
        }
        return true;
    }

    private boolean handleTeleport(CommandSender sender, String[] args, boolean spectator) {
        if (!sender.hasPermission(RECEIVE_PERMISSION)) {
            sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }
        if (args.length < 1 || args[0].isBlank()) {
            sender.sendMessage(Component.text("Usage: /report-teleport-" + (spectator ? "spec" : "normal") + " [player]", NamedTextColor.RED));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("That player is offline.", NamedTextColor.RED));
            return true;
        }

        Location targetLocation = target.getLocation();
        if (spectator) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        player.teleport(targetLocation);
        return true;
    }
}
