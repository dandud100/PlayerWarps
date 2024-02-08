package dev.revivalo.playerwarps.warp.actions;

import dev.revivalo.playerwarps.configuration.enums.Config;
import dev.revivalo.playerwarps.configuration.enums.Lang;
import dev.revivalo.playerwarps.utils.PermissionUtils;
import dev.revivalo.playerwarps.warp.Warp;
import dev.revivalo.playerwarps.warp.WarpAction;
import org.bukkit.entity.Player;

public class RenameAction implements WarpAction<String> {
    @Override
    public void execute(Player player, Warp warp, String newName) {
        int limit = Config.WARP_NAME_MAX_LENGTH.asInt();
        if (newName.length() > limit) {
            player.sendMessage(Lang.WARP_NAME_IS_ABOVE_LETTER_LIMIT.asColoredString().replace("%limit%", String.valueOf(limit)));
            return;
        }

        if (newName.contains(" ")) {
            player.sendMessage(Lang.NAME_CANT_CONTAINS_SPACE.asColoredString());
            return;
        }

        warp.setName(newName);
        player.sendMessage(Lang.WARP_RENAMED.asColoredString().replace("%oldName%", warp.getName()).replace("%newName%", newName));
    }

    @Override
    public PermissionUtils.Permission getPermission() {
        return PermissionUtils.Permission.RENAME_WARP;
    }

    @Override
    public int getFee() {
        return Config.RENAME_WARP_FEE.asInt();
    }
}
