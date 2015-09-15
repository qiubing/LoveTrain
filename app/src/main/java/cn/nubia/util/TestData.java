package cn.nubia.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LK on 2015/9/14.
 * 测试数据
 */
public class TestData {
    public static int count = 0;//测试使用计数

    public static JSONObject getCourseUserData() throws JSONException {
        String s = "{\"code\":\"0\",\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[],\"data\":[{\"UserID\":\"0016003333\",\"UserName\":\"张三\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003334\",\"UserName\":\"李四\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003335\",\"UserName\":\"王五\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003336\",\"UserName\":\"武清区\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003337\",\"UserName\":\"吕康\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003338\",\"UserName\":\"李康\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003339\",\"UserName\":\"小健\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003340\",\"UserName\":\"小明\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三1\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三2\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三3\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三4\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003334\",\"UserName\":\"李四\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003335\",\"UserName\":\"王五\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003336\",\"UserName\":\"武清区\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003337\",\"UserName\":\"吕康\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003338\",\"UserName\":\"李康\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003339\",\"UserName\":\"小健\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003340\",\"UserName\":\"小明\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三1\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三2\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三3\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三4\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003334\",\"UserName\":\"李四\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003335\",\"UserName\":\"王五\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003336\",\"UserName\":\"武清区\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003337\",\"UserName\":\"吕康\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003338\",\"UserName\":\"李康\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003339\",\"UserName\":\"小健\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003340\",\"UserName\":\"小明\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三1\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三2\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三3\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"},{\"UserID\":\"0016003333\",\"UserName\":\"张三4\",\"gender\":true,\"LastLoginTime\":\"2015-9-1 18:00:00\",\"RegisterTime\":\"2015-9-1 18:00:00\",\"UserTotalCredit\":150,\"UserIconURL\":\"\"}]}";
        return new JSONObject(s);
    }
    public static JSONObject getLoginResult() throws JSONException {
        String code = "0";
        String testStr = "{\"code\":" + getCode() + ",\"result\":\"success\",\"message\":[],\"field_errors\":{},\"errors\":[],\"data\":\"testdata\"}";
        return new JSONObject(testStr);
    }

    public static String getCode() {
        String code = "0";
        if (count % 7 == 0) {
            code = "0";
        } else if (count % 7 == 1) {
            code = "1000";
        } else if (count % 7 == 2) {
            code = "1001";
        } else if (count % 7 == 3) {
            code = "1002";
        } else if (count % 7 == 4) {
            code = "2001";
        } else if (count % 7 == 5) {
            code = "2002";
        } else if (count % 7 == 6) {
            code = "3000";
        }
        count++;
        return code;
    }
}
