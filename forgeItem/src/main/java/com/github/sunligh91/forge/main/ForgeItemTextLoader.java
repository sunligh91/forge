package com.github.sunligh91.forge.main;

public class ForgeItemTextLoader {

    public static String SPECIAL_NOTIFICATION;
    public static String PLUGIN_NAME;
    public static String PLUGIN_GUI_TITLE;
    public static String CHECK_MONEY_TRUE;
    public static String CHECK_MONEY_FALSE;
    public static String EQUIP_CHECK_TRUE;
    public static String EQUIP_CHECK_FALSE;


    public void init(ForgeItem plugin){
        PLUGIN_NAME = (String) plugin.getConfig().get("pluginName");
        PLUGIN_GUI_TITLE = (String) plugin.getConfig().get("pluginGuiTitle");
        CHECK_MONEY_TRUE = (String) plugin.getConfig().get("checkMoneyTrue");
        CHECK_MONEY_FALSE = (String) plugin.getConfig().get("checkMoneyFalse");
        EQUIP_CHECK_TRUE = (String) plugin.getConfig().get("equipCheckTrue");
        EQUIP_CHECK_FALSE = (String) plugin.getConfig().get("equipCheckFalse");
        SPECIAL_NOTIFICATION = (String) plugin.getConfig().get("specialNotification");
    }
}
