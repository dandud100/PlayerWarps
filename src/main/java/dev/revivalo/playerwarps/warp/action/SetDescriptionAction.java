package dev.revivalo.playerwarps.warp.action;

import dev.revivalo.playerwarps.configuration.file.Config;
import dev.revivalo.playerwarps.configuration.file.Lang;
import dev.revivalo.playerwarps.util.PermissionUtil;
import dev.revivalo.playerwarps.warp.Warp;
import dev.revivalo.playerwarps.warp.WarpAction;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SetDescriptionAction implements WarpAction<String> {
    @Override
    public boolean execute(Player player, Warp warp, String text) {
        int textLength = text.length();
        if (textLength < 5 || textLength > 32) {
            player.sendMessage(Lang.TEXT_SIZE_ERROR.asColoredString());
            return false;
        }

        warp.setDescription(text);
        player.sendMessage(Lang.DESCRIPTION_CHANGED.asReplacedString(player, new HashMap<String, String>() {{
            put("%warp%", warp.getName());
        }}));

        return true;
    }

    @Override
    public PermissionUtil.Permission getPermission() {
        return PermissionUtil.Permission.SET_DESCRIPTION;
    }

    @Override
    public int getFee() {
        return Config.SET_DESCRIPTION_FEE.asInteger();
    }

    @Override
    public Lang getInputText() {
        return null;
    }

    @Override
    public boolean isPublicAction() {
        return false;
    }
}