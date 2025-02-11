package net.buycraft.plugin.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import net.buycraft.plugin.velocity.BuycraftPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class InformationSubcommand implements Subcommand {
    private final BuycraftPlugin plugin;

    public InformationSubcommand(final BuycraftPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(Component.text(plugin.getI18n().get("no_params")).color(NamedTextColor.RED));
            return;
        }

        if (plugin.getApiClient() == null) {
            sender.sendMessage(Component.text(plugin.getI18n().get("need_secret_key")).color(NamedTextColor.RED));
            return;
        }

        if (plugin.getServerInformation() == null) {
            sender.sendMessage(Component.text(plugin.getI18n().get("information_no_server")).color(NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text(plugin.getI18n().get("information_title")).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text(plugin.getI18n().get("information_server",
                plugin.getServerInformation().getServer().getName(),
                plugin.getServerInformation().getAccount().getName())).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text(plugin.getI18n().get("information_currency",
                plugin.getServerInformation().getAccount().getCurrency().getIso4217())).color(NamedTextColor.GRAY));
        sender.sendMessage(Component.text(plugin.getI18n().get("information_domain",
                plugin.getServerInformation().getAccount().getDomain())).color(NamedTextColor.GRAY));
    }

    @Override
    public String getDescription() {
        return plugin.getI18n().get("usage_information");
    }
}
