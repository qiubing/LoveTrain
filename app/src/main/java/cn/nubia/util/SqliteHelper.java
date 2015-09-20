package cn.nubia.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by WJ on 2015/9/6.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    final static String TAG = "SqliteHelper";
    final static String TB_NAME_CLASS = "CLASS_INFO";
    final static String TB_NAME_LESSON = "LESSON_INFO";
    final static String TB_NAME_SIGNUP = "SIGNUP_INFO";

    final String CREATE_USERINFO_TABLE = "CREATE TABLE IF NOT EXISTS USER_INFO " +
            "(_id integer primary key autoincrement," +
            "user_id varchar(20)," +
            "user_password varchar(20)," +
            "gender bit," +
            "user_icon_url nvarchar," +
            "last_login_time Integer," +
            "register_time Integer," +
            "user_total_credits Integer," +
            "user_icon blob)";

    final String CREATE_CLASS_TABLE = "CREATE TABLE IF NOT EXISTS CLASS_INFO " +
            "(class_index Integer," +                    //课程索引
            "name varchar(50)," +                 //课程名称
            "description varchar(500)," +         //课程描述
            "type Integer," +                            //课程类型
            "share_type Integer," +                      //分享类型
            "course_status Integer," +                   //课程状态
            "hasExam Integer," +                             //是否有考试
            "lessones Integer," +
            "course_credits Integer," +                  //课程能获得的积分
            "course_record_modify_time Integer)";        //课程修改时间

    final String CREATE_LESSON_TABLE = "CREATE TABLE IF NOT EXISTS LESSON_INFO " +
            "(class_index Integer," +                    //课程索引
            "lesson_index Integer," +                    //课时索引
            "name varchar(50)," +                 //课时名称
            "teacher_id varchar(20)," +                  //讲师ID
            "teacher_name varchar(20)," +                //讲师姓名
            "description varchar(500)," +               //简介
            "start_time Integer," +                      //开始时间
            "end_time Integer," +                        //结束时间
            "locale varchar(50)," +                      //上课地点
            "check_credits Integer," +                   //签到积分
            "teacher_credits Integer," +                 //讲师能得到的积分
            "judge_score float," +                       //学员评分结果
            "lesson_record_modify_time Integer)" ;       //课时信息修改时间

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(name, null);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_LESSON_TABLE);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TB_NAME_CLASS);
        db.execSQL("DROP TABLE IF EXISTS" + TB_NAME_LESSON);
        onCreate(db);
        Log.d(TAG, "onUpgrade");
    }
}
