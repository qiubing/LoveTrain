package cn.nubia.util.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cn.nubia.entity.UserInfo;


/**
 * Created by JiangYu on 2015/9/10.
 */
public class UserInfoAssembler implements IAssemblerGenerics<UserInfo> {
    @Override
    public List<UserInfo> assemble(JSONArray jsonArray) {
        try {
            List<UserInfo> itemList = new ArrayList<UserInfo>();
            for(int arrayIndex=0;arrayIndex<jsonArray.length();arrayIndex++) {
                JSONObject jsonObject = jsonArray.getJSONObject(arrayIndex);
                String objectType = jsonObject.getString("type");
                switch (objectType) {
                    case "user":
                        itemList.add(makeUserInfo(jsonObject.getJSONObject("detail")));
                }
            }
            return itemList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
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
