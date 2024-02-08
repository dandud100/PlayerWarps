package dev.revivalo.playerwarps.guimanager.menu;

import dev.revivalo.playerwarps.PlayerWarpsPlugin;
import dev.revivalo.playerwarps.categories.Category;
import dev.revivalo.playerwarps.categories.CategoryManager;
import dev.revivalo.playerwarps.configuration.enums.Config;
import dev.revivalo.playerwarps.configuration.enums.Lang;
import dev.revivalo.playerwarps.user.UserManager;
import dev.revivalo.playerwarps.utils.DateUtils;
import dev.revivalo.playerwarps.utils.NumberUtils;
import dev.revivalo.playerwarps.utils.SortingUtils;
import dev.revivalo.playerwarps.utils.TextUtils;
import dev.revivalo.playerwarps.warp.Warp;
import dev.revivalo.playerwarps.warp.actions.FavoriteWarpAction;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WarpsMenu implements Menu {
    private final int page;
    private final MenuType menuType;
    private final PaginatedGui paginatedGui;

    public WarpsMenu(MenuType menuType, int page) {
        this.menuType = menuType;
        this.page = page;
        this.paginatedGui = Gui.paginated()
                .pageSize(45)
                .rows(6)
                .title(Component.text(getMenuType().getTitle().replace("%page%", String.valueOf(page))))
                .disableAllInteractions()
                .create();
    }

    @Override
    public MenuType getMenuType() {
        return menuType;
    }

    @Override
    public void open(Player player) {
        open(player, "all", SortingUtils.SortType.LATEST);
    }

    public void open(Player player, String categoryName, SortingUtils.SortType sortType) {
        final Category openedCategory = CategoryManager.getCategoryFromName(categoryName);

        if (paginatedGui.previous())
            paginatedGui.setItem(45, ItemBuilder.from(Material.ARROW).name(Component.text(Lang.PREVIOUS_PAGE.asColoredString())).asGuiItem(event -> {
                paginatedGui.previous();
                paginatedGui.updateTitle(getMenuType().getTitle().replace("%page%", String.valueOf(paginatedGui.getCurrentPageNum())));
            }));

        if (paginatedGui.next())
            paginatedGui.setItem(53, ItemBuilder.from(Material.ARROW).name(Component.text(Lang.NEXT_PAGE.asColoredString())).asGuiItem(event -> {
                paginatedGui.next();
                paginatedGui.updateTitle(getMenuType().getTitle().replace("%page%", String.valueOf(paginatedGui.getCurrentPageNum())));
            }));

        SortingUtils.SortType nextSortType = sortType == SortingUtils.SortType.LATEST ? SortingUtils.SortType.VISITS
                : sortType == SortingUtils.SortType.VISITS
                ? SortingUtils.SortType.RATING : SortingUtils.SortType.LATEST;

        if (getMenuType() != MenuType.OWNED_LIST_MENU) paginatedGui.setItem(46, ItemBuilder.from(Material.REPEATER)
                .name(Component.text(Lang.SORT_WARPS.asColoredString()))
                .lore(Stream.of(
                                " ",
                                TextUtils.colorize(sortType == SortingUtils.SortType.LATEST ? "&a" : "&7") + "► " + Lang.LATEST.asColoredString(),
                                TextUtils.colorize(sortType == SortingUtils.SortType.VISITS ? "&a" : "&7") + "► " + Lang.VISITS.asColoredString(),
                                TextUtils.colorize(sortType == SortingUtils.SortType.RATING ? "&a" : "&7") + "► " + Lang.RATING.asColoredString(),
                                " ",
                                Lang.CLICK_TO_SORT_BY.asReplacedString(new HashMap<String, String>() {{
                                    put("%selector%", nextSortType.getName());
                                }})
                        )
                        .map(Component::text)
                        .collect(Collectors.toList()))
                .asGuiItem(event -> {
                    paginatedGui.clearPageItems();
                    open(player, categoryName, nextSortType);
                })); //openWarpsMenu(player, menuType, category, page, nextSortType)));

        final List<Warp> warps = new ArrayList<>();
        switch (getMenuType()) {
            case DEFAULT_LIST_MENU:
                if (openedCategory.isDefaultCategory()) {
                    warps.addAll(PlayerWarpsPlugin.getWarpHandler().getWarps().stream()
                            .filter(Warp::isAccessible)
                            .collect(Collectors.toList()));
                } else {
                    warps.addAll(PlayerWarpsPlugin.getWarpHandler().getWarps().stream()
                            .filter(warp -> warp.isAccessible() && (warp.getCategory() == null || warp.getCategory().getType().equalsIgnoreCase(categoryName)))
                            .collect(Collectors.toList()));
                }
                warps.sort(sortType.getComparator());

                break;
            case OWNED_LIST_MENU:
                warps.addAll(PlayerWarpsPlugin.getWarpHandler().getWarps().stream().filter(warp -> warp.canManage(player)).collect(Collectors.toList()));
                break;
            case FAVORITE_LIST_MENU:
                warps.addAll(PlayerWarpsPlugin.getWarpHandler().getPlayerFavoriteWarps(player));
                break;
        }

        final Lang warpLore = getMenuType() == MenuType.OWNED_LIST_MENU ? Lang.OWN_WARP_LORE : Lang.WARP_LORE;

        AtomicReference<GuiItem> guiItem = new AtomicReference<>();
        warps.forEach(warp -> {
            if (warp.getLocation() == null) {
                guiItem.set(new GuiItem(ItemBuilder.from(Material.BARRIER)
                        .setName("§4" + warp.getName())
                        .setLore(Lang.WARP_IN_DELETED_WORLD.asColoredString())
                        .build()));
            } else {
                guiItem.set(new GuiItem(ItemBuilder.from(warp.getMenuItem())
                        .name(Component.text(TextUtils.colorize(Config.WARP_NAME_FORMAT.asString().replace("%warpName%", warp.getDisplayName()))))
                        .lore(warpLore.asReplacedList(new HashMap<String, String>() {{
                                                          put("%creationDate%", DateUtils.getFormatter().format(warp.getDateCreated()));
                                                          put("%world%", warp.getLocation().getWorld().getName());
                                                          put("%voters%", String.valueOf(warp.getReviewers().size()));
                                                          put("%price%", warp.getAdmission() == 0
                                                                  ? Lang.FREE_OF_CHARGE.asColoredString()
                                                                  : TextUtils.formatNumber(warp.getAdmission()) + " " + Config.CURRENCY_SYMBOL.asString());
                                                          put("%today%", String.valueOf(warp.getTodayVisits()));
                                                          put("%status%", warp.getStatus().getText());
                                                          put("%ratings%", String.valueOf(NumberUtils.round(warp.getConvertedRating(), 1)));
                                                          put("%stars%", TextUtils.createRatingFormat(warp));
                                                          put("%lore%", warp.getDescription() == null
                                                                  ? Lang.NO_DESCRIPTION.asColoredString()
                                                                  : warp.getDescription());
                                                          put("%visits%", String.valueOf(warp.getVisits()));
                                                          put("%owner-name%", Objects.requireNonNull(Bukkit.getOfflinePlayer(warp.getOwner()).getName()));
                                                      }}
                        )).build()));

                guiItem.get().setAction(event -> {
                    if (PlayerWarpsPlugin.getWarpHandler().isWarps()) {
                        switch (event.getClick()) {
                            case LEFT:
                                player.closeInventory();
                                PlayerWarpsPlugin.getWarpHandler().preWarp(player, warp);
                                break;
                            case RIGHT:
                            case SHIFT_RIGHT:
                                UserManager.createUser(player, new Object[]{paginatedGui.getCurrentPageNum()});
                                if (getMenuType() == MenuType.OWNED_LIST_MENU) {
                                    if (!player.hasPermission("playerwarps.settings")) {
                                        player.sendMessage(Lang.INSUFFICIENT_PERMS.asColoredString());
                                        return;
                                    }
                                    new ManageMenu(warp).open(player); //openSetUpMenu(player, warp);
                                } else {
                                    new ReviewMenu(warp).open(player); //openReviewMenu(player, warp);
                                }
                                break;
                            case SHIFT_LEFT:
                                new FavoriteWarpAction().preExecute(player, warp, null, MenuType.FAVORITE_LIST_MENU);

                                break;
                        }
                    }
                });
            }
            paginatedGui.addItem(guiItem.get());
        });
        //}

        setDefaultItems(player, paginatedGui);
        //createGuiItems(player, paginatedGui, menuType);
        paginatedGui.open(player, page);
    }
}