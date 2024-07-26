package sullexxx.ultimatereport;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class UltimateReport extends JavaPlugin {
    private static UltimateReport instance;
    public static UltimateReport getInstance() {
        return instance;
    }
    private JDA jda;
    private TextChannel channel;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();
        discordStart();
        getCommand("ultimatereport").setExecutor(new UltimateReportCommand(this));
        getCommand("ultimatereport").setTabCompleter(new UltimateReportCommand(this));
        getCommand("report").setExecutor(new UltimateReportCommandReport(this));
        getCommand("report").setTabCompleter(new UltimateReportCommandReport(this));
        getLogger().info("\n" +
                "─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────\n" +
                "─██████████████─██████──██████─██████─────────██████─────────██████████████─██████████████─████████──████████─████████──████████─\n" +
                "─██░░░░░░░░░░██─██░░██──██░░██─██░░██─────────██░░██─────────██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░██──██░░░░██─██░░░░██──██░░░░██─\n" +
                "─██░░██████████─██░░██──██░░██─██░░██─────────██░░██─────────██░░██████████─██░░██████████─████░░██──██░░████─████░░██──██░░████─\n" +
                "─██░░██─────────██░░██──██░░██─██░░██─────────██░░██─────────██░░██─────────██░░██───────────██░░░░██░░░░██─────██░░░░██░░░░██───\n" +
                "─██░░██████████─██░░██──██░░██─██░░██─────────██░░██─────────██░░██████████─██░░██████████───████░░░░░░████─────████░░░░░░████───\n" +
                "─██░░░░░░░░░░██─██░░██──██░░██─██░░██─────────██░░██─────────██░░░░░░░░░░██─██░░░░░░░░░░██─────██░░░░░░██─────────██░░░░░░██─────\n" +
                "─██████████░░██─██░░██──██░░██─██░░██─────────██░░██─────────██░░██████████─██░░██████████───████░░░░░░████─────████░░░░░░████───\n" +
                "─────────██░░██─██░░██──██░░██─██░░██─────────██░░██─────────██░░██─────────██░░██───────────██░░░░██░░░░██─────██░░░░██░░░░██───\n" +
                "─██████████░░██─██░░██████░░██─██░░██████████─██░░██████████─██░░██████████─██░░██████████─████░░██──██░░████─████░░██──██░░████─\n" +
                "─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░░░░░░░██─██░░░░██──██░░░░██─██░░░░██──██░░░░██─\n" +
                "─██████████████─██████████████─██████████████─██████████████─██████████████─██████████████─████████──████████─████████──████████─\n" +
                "─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        getLogger().info("GitHub: https://github.com/sulleexx   |  Discord: sullexxx");
    }

    @Override
    public void onDisable() {

    }

    public void discordStart() {
        try {
            jda = JDABuilder.createDefault(getConfig().getString("General.Token")).build();
            jda.awaitReady();
            channel = jda.getTextChannelById(getConfig().getString("General.IDChannelReport"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendReport(Player player, String offender, String description) {
        if (jda != null && channel != null) {
            EmbedBuilder eb = new EmbedBuilder();
            String title = getConfig().getString("ReportFormat.Title", " ");
            eb.setTitle(title);
            String descEmb = getConfig().getString("ReportFormat.Description", " ");
            eb.setDescription(descEmb);
            eb.addField(
                    getConfig().getString("ReportFormat.field-1.Name", "Field 1"),
                    getConfig().getString("ReportFormat.field-1.Value", "Unknown").replace("{sender}", player.getName()),
                    getConfig().getBoolean("ReportFormat.field-1.InLine", true)
            );
            eb.addField(
                    getConfig().getString("ReportFormat.field-2.Name", "Field 2"),
                    getConfig().getString("ReportFormat.field-2.Value", "Unknown").replace("{offender}", offender),
                    getConfig().getBoolean("ReportFormat.field-2.InLine", true)
            );
            eb.addField(
                    getConfig().getString("ReportFormat.field-3.Name", "Field 3"),
                    getConfig().getString("ReportFormat.field-3.Value", "Unknown").replace("{report_text}", description),
                    getConfig().getBoolean("ReportFormat.field-3.InLine", true)
            );
            try {
                eb.setColor(Color.decode(getConfig().getString("ReportFormat.color", "#000000")));
            } catch (NumberFormatException e) {
                eb.setColor(Color.ORANGE);
                System.err.println("Invalid color in config. Using default color");
            }
            if (getConfig().getBoolean("ReportFormat.thumbnail.enable", false)) {
                String thumbnailUrl = getConfig().getString("ReportFormat.thumbnail.url");
                if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                    eb.setThumbnail(thumbnailUrl);
                } else {
                    System.err.println("Thumbnail enabled, but URL is invalid. Ignore thumbnail.");
                }
            }
            if (getConfig().getBoolean("ReportFormat.image.enable", false)) {
                String imageUrl = getConfig().getString("ReportFormat.image.url");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    eb.setImage(imageUrl);
                } else {
                    System.err.println("Image enabled, but URL is invalid. Ignore image.");
                }
            }
            channel.sendMessage(eb.build()).queue();
        }
    }
}
