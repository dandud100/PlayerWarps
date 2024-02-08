package dev.revivalo.playerwarps.utils;

import org.bukkit.command.CommandSender;

public final class PermissionUtils {
    public static boolean hasPermission(CommandSender commandSender, Permission permission){
        if (commandSender.isOp())
            return true;

        else if (commandSender.hasPermission(Permission.ADMIN_PERMISSION.get()))
            return true;

        else
            return commandSender.hasPermission(permission.get());
    }

    public enum Permission {
        ADMIN_PERMISSION("playerwarps.admin"),
        RELOAD_PLUGIN("playerwarps.reload"),
        USE("playerwarps.use"),
        MANAGE("playerwarps.manage"),
        HELP("playerwarps.help"),
        FAVORITE_WARP("playerwarps.favorite"),
        REVIEW_WARP("playerwarps.review"),
        CREATE_WARP("playerwarps.create"),
        REMOVE_WARP("playerwarps.remove"),
        TRANSFER_WARP("playerwarps.transfer"),
        RENAME_WARP("playerwarps.rename"),
        RELOCATE_WARP("playerwarps.relocate"),
        SET_WARP_TYPE("playerwarps.settings.settype"),
        SET_PREVIEW_ITEM("playerwarps.settings.setitem"),
        SET_DESCRIPTION("playerwarps.settings.setdescription"),
        SET_ADMISSION("playerwarps.settings.setadmission"),
        SET_STATUS("playerwarps.settings.setstatus"),
        CHANGE_DISPLAY_NAME("playerwarps.settings.changedisplayname"),
        BYPASS_TELEPORT_DELAY("playerwarps.delay.bypass"),
        MANAGE_OTHERS("playerwarps.manage.others");

        private final String permission;
        Permission(String permission) {
            this.permission = permission;
        }

        public String get() {
            return permission;
        }
    }
}
