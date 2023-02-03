package cz.revivalo.playerwarps.configuration.enums;

import com.google.common.base.Splitter;
import cz.revivalo.playerwarps.PlayerWarps;
import cz.revivalo.playerwarps.configuration.YamlFile;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Lang {
    CATEGORIES_ARE_DISABLED("categories-are-disabled"),
    ENTERED_HIGHER_PRICE_THAN_ALLOWED("entered-higher-price-than-allowed"),
    ACCEPT_TELEPORT_WITH_ADMISSION("accept-teleport-with-admission"),
    TELEPORT_TO_WARP_WITH_ADMISSION("teleport-to-warp-with-admission"),
    HELP("help-message-lore"),
    OWN_WARP_ITEM_NAME("own-warp-item-name"),
    RELOAD("reload-message"),
    LIMIT_REACHED("limit-reached"),
    NO_WARPS("no-warps"),
    NON_EXISTING_WARP("non-existing-warp"),
    WARP_ALREADY_CREATED("warp-already-created"),
    WARP_CREATED("warp-created"),
    WARP_CREATED_WITH_PRICE("warp-created-with-price"),
    INSUFFICIENT_BALANCE("insufficient-balance"),
    INSUFFICIENT_BALANCE_TO_TELEPORT("insufficient-balance-to-teleport"),
    WARP_REMOVED("warp-removed"),
    WARP_REMOVED_WITH_REFUND("warp-removed-with-refund"),
    TELEPORTATION("teleportation"),
    TELEPORTATION_CANCELLED("teleportation-cancelled"),
    TELEPORT_TO_WARP("teleport-to-warp"),
    NAME_CANT_CONTAINS_DOT("name-cant-contains-dot"),
    INSUFFICIENT_PERMS("insufficient-perms"),
    ITEM_CHANGED("item-changed"),
    TITLE_WRITE_MSG("title-write-msg"),
    RENAME_MSG("rename-write-msg"),
    SET_DESCRIPTION_MESSAGE("set-description-msg"),
    ITEM_WRITE_MSG("item-write-msg"),
    TEXT_WRITE_CANCELED("text-write-canceled"),
    TEXT_SIZE_ERROR("text-size-error"),
    TEXT_CHANGED("text-changed"),
    NOT_OWNING("not-owning"),
    INVALID_ITEM("invalid-item"),
    ALREADY_REVIEWED("already-reviewed"),
    INVALID_REVIEW("invalid-review"),
    SELF_REVIEW("self-review"),
    WARP_REVIEWED("warp-reviewed"),
    TRIED_TO_SET_BANNED_ITEM("tried-to-set-banned-item"),
    NOT_A_NUMBER("not-a-number"),
    PRICE_WRITE_MESSAGE("price-write-msg"),
    PRICE_CHANGED("price-changed"),
    INVALID_ENTERED_PRICE("invalid-entered-price"),
    WARPS_ITEM_NAME("warps-item-name"),
    MY_WARPS_ITEM_NAME("my-warps-item-name"),
    REVIEW_WARP_TITLE("review-warp-title"),
    FAVORITE_WARPS_ITEM_NAME("favorite-warps-item-name"),
    MY_WARPS_TITLE("my-warp-title"),
    WARPS_TITLE("warps-title"),
    EDIT_WARP_MENU_TITLE("edit-warp-menu-title"),
    FAVORITES_TITLE("favorites-title"),
    ACCEPT_MENU_TITLE("accept-menu-title"),
    ACCEPT("accept"),
    DENY("deny"),
    SORTER("warps-sorter"),
    NEXT_PAGE("next-page"),
    PREVIOUS_PAGE("previous-page"),
    NO_DESCRIPTION("no-description"),
    BACK_NAME("back-name"),
    ONE_STAR("1star"),
    TWO_STARS("2star"),
    THREE_STARS("3star"),
    FOUR_STARS("4star"),
    FIVE_STARS("5star"),
    WARP_ENABLED("warp-enabled"),
    WARP_DISABLED("warp-disabled"),
    WARP_IS_DISABLED("warp-is-disabled"),
    ENTERED_INVALID_TYPE("entered-invalid-type-of-warp"),
    WARP_TYPE_CHANGED("warp-type-changed"),
    PRIVACY_CHANGED("privacy-changed"),
    BAD_COMMAND_SYNTAX("bad-command-syntax"),
    ADD_FAVORITE("favorite-warp-added"),
    REMOVE_FAVORITE("remove-favorite-warp"),
    FAV_NOT_CONTAINS("warp-not-contains-favlist"),
    ALREADY_FAVORITE("warp-already-contains-favlist"),
    TRANSFER_SUCCESSFUL("ownership-transfer-successful"),
    TRANSFER_INFO("ownership-transfer-info"),
    TRANSFER_ERROR("ownership-transfer-error"),
    OWNER_CHANGE_MSG("owner-change-msg"),
    WARP_RENAMED("warp-name-changed"),
    TRIED_TO_CREATE_PWARP_IN_DISABLED_WORLD("tried-to-create-warp-in-disabled-world"),
    WARP_NAME_IS_ABOVE_LETTER_LIMIT("warp-name-is-above-letters-limit"),
    CATEGORY_TITLE("category-title"),
    FREE_OF_CHARGE("free-of-charge"),
    WARP_ACTIVE("warp-active"),
    WARP_INACTIVE("warp-inactive"),
    REMOVE_WARP("remove-warp"),
    CHANGE_TYPE("change-type"),
    CHANGE_ITEM("change-item"),
    CHANGE_DESCRIPTION("change-label"),
    PWARP_ENABLE("pwarp-enable"),
    PRIVACY("privacy"),
    SET_PRICE("set-price"),
    CHANGE_OWNER("change-owner"),
    RENAME_WARP("rename-warp"),
    WARP_LORE("warp-lore"),
    OWN_WARP_LORE("own-warp-lore"),
    REMOVE_WARP_LORE("remove-warp-lore"),
    CHANGE_TYPE_LORE("change-type-lore"),
    CHANGE_ITEM_LORE("change-item-lore"),
    CHANGE_DESCRIPTION_LORE("change-label-lore"),
    PWARP_ENABLE_LORE("pwarp-enabled-lore"),
    PWARP_DISABLE_LORE("pwarp-disabled-lore"),
    PRIVATE_ENABLE_LORE("private-enabled-lore"),
    PRIVATE_DISABLE_LORE("private-disabled-lore"),
    SET_PRICE_LORE("set-price-lore"),
    CHANGE_OWNER_LORE("change-owner-lore"),
    RENAME_WARP_LORE("rename-warp-lore");

    private static final Map<String, String> messages = new HashMap<>();
    private static final Map<String, String> listsStoredAsString = new HashMap<>();
    private static final Pattern hexPattern = Pattern.compile("<#([A-Fa-f\\d]){6}>");
    private final String text;

    public static void reload() {
        final YamlConfiguration configuration = new YamlFile(String.format("lang%s%s.yml", File.separator, Config.LANGUAGE.asString()),
                PlayerWarps.getPlugin().getDataFolder())
                .getConfiguration();

        final ConfigurationSection languageSection = configuration.getConfigurationSection("lang");
        Objects.requireNonNull(languageSection)
                .getKeys(false)
                .forEach(key -> {
                    if (key.endsWith("lore") || key.endsWith("notification") || key.endsWith("help")) {
                        listsStoredAsString.put(key, applyColor(String.join("⎶", languageSection.getStringList(key))));
                        return;
                    }
                    messages.put(key, applyColor(languageSection.getString(key)));
                });
    }

    @SuppressWarnings("deprecation")
    public static String applyColor(String message) {
        if (PlayerWarps.isHexSupported()) {
            Matcher matcher = hexPattern.matcher(message);
            while (matcher.find()) {
                message = String.format("%s%s%s", message.substring(0, matcher.start()),
                        ChatColor.valueOf(matcher.group().substring(1, matcher.group().length() - 1)),
                        message.substring(matcher.end()));
                matcher = hexPattern.matcher(message);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> applyColor(List<String> list){
        return list.stream().map(Lang::applyColor).collect(Collectors.toList());
    }

    public static String replaceString(String messageToReplace, final Map<String, String> definitions){
        final String[] keys = definitions.keySet().toArray(new String[0]);
        final String[] values = definitions.values().toArray(new String[0]);

        return StringUtils.replaceEach(messageToReplace, keys, values);
    }

    public static String stripColor(String input){
        return ChatColor.stripColor(input);
    }

    public static void sendListToPlayer(final Player player, final List<String> list) {
        list.forEach(player::sendMessage);
    }

    public List<String> asColoredList(){
        return Splitter.on("⎶").splitToList(listsStoredAsString.get(this.text));
    }

    public List<String> asReplacedList(final Map<String, String> definitions) {
        final String loreAsString = listsStoredAsString.get(this.text);
        final String[] keys = definitions.keySet().toArray(new String[]{});
        final String[] values = definitions.values().toArray(new String[]{});

        return Splitter.on("⎶").splitToList(StringUtils.replaceEach(loreAsString, keys, values));
    }

    public String asColoredString() {
        return messages.get(text);
    }

    public String asReplacedString(Map<String, String> definitions){
        return replaceString(messages.get(text), definitions);
    }
}
