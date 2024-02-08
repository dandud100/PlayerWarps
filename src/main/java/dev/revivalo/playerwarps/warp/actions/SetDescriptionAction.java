package dev.revivalo.playerwarps.warp.actions;

import dev.revivalo.playerwarps.configuration.enums.Config;
import dev.revivalo.playerwarps.configuration.enums.Lang;
import dev.revivalo.playerwarps.utils.PermissionUtils;
import dev.revivalo.playerwarps.warp.Warp;
import dev.revivalo.playerwarps.warp.WarpAction;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SetDescriptionAction implements WarpAction<String> {
    @Override
    public void execute(Player player, Warp warp, String text) {
        int textLength = text.length();
        if (textLength < 5 || textLength > 32) {
            player.sendMessage(Lang.TEXT_SIZE_ERROR.asColoredString());
            return;
        }

        warp.setDescription(text);
        player.sendMessage(Lang.DESCRIPTION_CHANGED.asReplacedString(new HashMap<String, String>() {{
            put("%warp%", warp.getName());
        }}));
    }

    @Override
    public PermissionUtils.Permission getPermission() {
        return PermissionUtils.Permission.SET_DESCRIPTION;
    }

    @Override
    public int getFee() {
        return Config.SET_DESCRIPTION_FEE.asInt();
    }
}
