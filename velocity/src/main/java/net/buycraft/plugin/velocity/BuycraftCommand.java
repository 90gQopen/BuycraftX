package net.buycraft.plugin.velocity;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandInvocation;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import net.buycraft.plugin.velocity.command.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuycraftCommand implements SimpleCommand {
    private final Map<String, Subcommand> subcommandMap = new LinkedHashMap<>();
    private final BuycraftPlugin plugin;

    public BuycraftCommand(BuycraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        
        if (!sender.hasPermission("buycraft.admin")) {
            sender.sendMessage(Component.text(plugin.getI18n().get("no_permission")).color(NamedTextColor.RED));
            return;
        }
        
        String[] args = invocation.arguments();
        
        if (args.length == 0) {
            showHelp(sender);
            return;
        }

        for (Map.Entry<String, Subcommand> entry : subcommandMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(args[0])) {
                String[] withoutSubcommand = Arrays.copyOfRange(args, 1, args.length);
                entry.getValue().execute(sender, withoutSubcommand);
                return;
            }
        }

        showHelp(sender);
    }

    @Override
    public List<String> suggest(final SimpleCommand.Invocation invocation) {
        return ImmutableList.of();
    }

    @Override
    public boolean hasPermission(final SimpleCommand.Invocation invocation) {
        return invocation.source().getPermissionValue("buycraft.admin") == Tristate.TRUE;
    }

    private void showHelp(CommandSource sender) {
        sender.sendMessage(Component.text(plugin.getI18n().get("usage")).color(NamedTextColor.DARK_AQUA).decoration(TextDecoration.BOLD, true));

        for (Map.Entry<String, Subcommand> entry : subcommandMap.entrySet()) {
            sender.sendMessage(Component.text("/tebex " + entry.getKey()).color(NamedTextColor.GREEN).append(Component.text(": " + entry.getValue().getDescription())));
        }
    }

    public Map<String, Subcommand> getSubcommandMap() {
        return this.subcommandMap;
    }
}
