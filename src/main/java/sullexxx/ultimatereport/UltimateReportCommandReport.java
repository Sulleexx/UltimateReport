package sullexxx.ultimatereport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UltimateReportCommandReport implements CommandExecutor, TabCompleter {
    private final UltimateReport plugin;

    public UltimateReportCommandReport(UltimateReport plugin) {
        this.plugin = plugin;
        plugin.getCommand("report").setExecutor(this);
        plugin.getCommand("report").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(FormatterMessages.getFormattedString("Messages.NotPlayer"));
            return true;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage(FormatterMessages.getFormattedString("Messages.InsufficientArguments"));
            return true;
        }

        String offender = args[0];
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        plugin.sendReport(player, offender, description);

        player.sendMessage(FormatterMessages.getFormattedString("Messages.ReportSent"));
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                completions.add(onlinePlayer.getName());
            }
        }
        return completions;
    }
}