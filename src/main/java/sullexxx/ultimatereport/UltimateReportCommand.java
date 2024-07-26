package sullexxx.ultimatereport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UltimateReportCommand implements CommandExecutor, TabCompleter {
    private final UltimateReport plugin;

    public UltimateReportCommand(UltimateReport plugin) {
        this.plugin = plugin;
        plugin.getCommand("ultimatereport").setExecutor(this);
        plugin.getCommand("ultimatereport").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(FormatterMessages.getFormattedString("Messages.NotPlayer"));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(UltimateReport.getInstance().getConfig().getString("General.Permission-Admin"))) {
            player.sendMessage(FormatterMessages.getFormattedString("Messages.NoPermission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(FormatterMessages.getFormattedString("Messages.UnknownCommand"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(player);
                break;
            default:
                player.sendMessage(FormatterMessages.getFormattedString("Messages.UnknownCommand"));
                break;
        }

        return true;
    }

    private void handleReload(Player player) {
        plugin.reloadConfig();
        player.sendMessage(FormatterMessages.getFormattedString("Messages.SuccessfulReload"));
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
        }
        return completions;
    }
}
