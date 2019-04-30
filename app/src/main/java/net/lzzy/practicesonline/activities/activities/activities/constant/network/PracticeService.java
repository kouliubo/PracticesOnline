package net.lzzy.practicesonline.activities.activities.activities.constant.network;

import net.lzzy.practicesonline.activities.activities.activities.constant.constants.ApiConstants;
import net.lzzy.practicesonline.activities.activities.activities.constant.models.Practice;
import net.lzzy.sqllib.JsonConverter;

import java.io.IOException;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/4/22.
 * Description:
 */
public class PracticeService {
    public static String getPracticesFromServer() throws IOException {
        return ApiService.okGet(ApiConstants.URL_PRACTICES);
    }
    public static List<Practice> getPractices(String json) throws Exception {
        JsonConverter<Practice> converter = new JsonConverter<Practice>(Practice.class);
        return converter.getArray(json);
    }
}
