package cn.nubia.util.jsonprocessor;

import java.util.List;

import cn.nubia.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JiangYu on 2015/9/10.
 */
public class UserInfoAssembler implements AssemblerGenerics<UserInfo> {
    @Override
    public boolean assemble(JSONArray jsonArray, List<UserInfo> list) {
        try {
            int arrayIndex = 0;
            JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
            while (jsonObject != null){
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "user":
                        list.add(makeUserInfo(jsonObject.getJSONObject("detail")));
                }
                jsonObject = jsonArray.getJSONObject(++arrayIndex);
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserInfo makeUserInfo(JSONObject jsonObject) throws JSONException {
        UserInfo userInfo = new UserInfo();

        userInfo.setUserID(jsonObject.getString("userid"));
        userInfo.setUserName(jsonObject.getString("username"));
        userInfo.setUserPasswd(jsonObject.getString("psword"));
        userInfo.setGender(jsonObject.getBoolean("gender"));
        userInfo.setUserIconURL(jsonObject.getString("usericonurl"));
        userInfo.setLastLoginTime(jsonObject.getLong("logintime"));
        userInfo.setRegisterTime(jsonObject.getLong("registertime"));
        userInfo.setUserTotalCredits(jsonObject.getInt("totalcredits"));
//        userInfo.setUserIcon(Drawable userIcon);

        return userInfo;
    }
}
