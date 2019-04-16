package net.lzzy.practicesonline.activities.activities.activities.constant.constants;

import net.lzzy.practicesonline.activities.activities.activities.constant.utils.AppUtils;

public class ApiConstants {
    private static final String IP = AppUtils.loadServerSetting(AppUtils.getContext()).first;
    private static final String PORT = AppUtils.loadServerSetting(AppUtils.getContext()).second;
    private static final String PROTOCOL = "http://";
    /**
     * API地址
     */
    public static final String URL_API = PROTOCOL.concat(IP).concat(":").concat(PORT);
}
