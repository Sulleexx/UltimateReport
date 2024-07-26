package sullexxx.ultimatereport;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class FormatterMessages {
    private static final UltimateReport ultimateReport = UltimateReport.getInstance();
    private static final FileConfiguration config = ultimateReport.getConfig();
    private static final LegacyComponentSerializer unusualHexSerializer = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component getFormattedString(String path) {
        String rawString = config.getString(path, "undefined");
        rawString = replacePlaceholders(rawString);
        return doubleFormat(rawString);
    }

    public static String getString(String path) {
        String rawString = config.getString(path, "undefined");
        rawString = replacePlaceholders(rawString);
        return rawString;
    }

    public static Component getFormattedString(String path, String player) {
        String rawString = config.getString(path, "undefined");
        rawString = replacePlaceholders(rawString).replace("{player}", player);
        return doubleFormat(rawString);
    }

    @NotNull
    private static String replacePlaceholders(@NotNull String rawString) {
        return rawString.replace("{prefix}", config.getString("General.Plugin-Prefix", ""));
    }

    @NotNull
    public static Component doubleFormat(@NotNull String message) {
        message = message.replace('§', '&');
        Component component = MiniMessage.miniMessage().deserialize(message).decoration(TextDecoration.ITALIC, false);
        String legacyMessage = toLegacy(component);
        legacyMessage = ChatColor.translateAlternateColorCodes('&', legacyMessage);
        return unusualHexSerializer.deserialize(legacyMessage);
    }

    public static String toLegacy(Component component) {
        return unusualHexSerializer.serialize(component);
    }
}
