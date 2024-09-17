package dev.revivalo.playerwarps.guimanager.menu;

import dev.revivalo.playerwarps.configuration.file.Config;
import dev.revivalo.playerwarps.configuration.file.Lang;
import dev.revivalo.playerwarps.util.ItemUtil;
import dev.revivalo.playerwarps.warp.Warp;
import dev.revivalo.playerwarps.warp.WarpAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ConfirmationMenu implements Menu {
    private final Warp warp;
    private final Gui gui;

    private static final ItemBuilder ACCEPT_ITEM = ItemBuilder.from(ItemUtil.getItem(Config.CONFIRM_ITEM.asUppercase())).setName(Lang.ACCEPT.asColoredString());
    private static final ItemBuilder DENY_ITEM = ItemBuilder.from(ItemUtil.getItem(Config.DENY_ITEM.asUppercase())).setName(Lang.DENY.asColoredString());

    public ConfirmationMenu(Warp warp) {
        this.warp = warp;
        this.gui = Gui.gui()
                .disableAllInteractions()
                .rows(3)
                .title(Component.text(Lang.CONFIRMATION_MENU_TITLE.asReplacedString(new HashMap<String, String>() {{
                    put("%warp%", warp.getName());
                }})))
                .create();
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.CONFIRMATION_MENU;
    }

    @Override
    public void open(Player player) {
        open(player, null);
    }

    public void open(Player player, WarpAction<?> action) {
        for (String position : Config.CONFIRM_ITEM_POSITIONS.asList()) {
            int slot = Integer.parseInt(position);
            gui.setItem(slot, ACCEPT_ITEM.asGuiItem(event -> {
                action.preExecute(player, warp, null, null);
                gui.close(player);
            }));
        }

        for (String position : Config.DENY_ITEM_POSITIONS.asList()) {
            int slot = Integer.parseInt(position);
            gui.setItem(slot, DENY_ITEM.asGuiItem(event -> {
                gui.close(player);
            }));
        }

        gui.open(player);
    }
}