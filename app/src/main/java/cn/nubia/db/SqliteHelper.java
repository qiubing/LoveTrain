package cn.nubia.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by WJ on 2015/9/6.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    final private String TAG = "SqliteHelper";
    final public static String TB_NAME_CLASS = "ALL_CLASS_INFO";
    final public static String TB_NAME_LESSON = "ALL_LESSON_INFO";
    final public static String TB_NAME_EXAM = "ALL_EXAM_INFO";
    final public static String TB_NAME_MY_CLASS_STU = "MY_CLASS_STU_INFO";
    final public static String TB_NAME_MY_CLASS_TEACHER = "MY_CLASS_TEACHER_INFO";

//    final static String CREATE_USERINFO_TABLE = "CREATE TABLE IF NOT EXISTS USER_INFO " +
//            "(_id integer primary key autoincrement," +
//            "user_id varchar(20)," +
//            "user_password varchar(20)," +
//            "gender bit," +
//            "user_icon_url nvarchar," +
//            "last_login_time Integer," +
//            "register_time Integer," +
//            "user_total_credits Integer," +
//            "user_icon blob)";

    private final static String CREATE_MYCLASS_STU_TABLE = "CREATE TABLE IF NOT EXISTS "+ TB_NAME_MY_CLASS_STU +
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

    private final static String CREATE_MYCLASS_TEACHER_TABLE = "CREATE TABLE IF NOT EXISTS "+ TB_NAME_MY_CLASS_TEACHER +
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

    private final static String CREATE_CLASS_TABLE = "CREATE TABLE IF NOT EXISTS "+TB_NAME_CLASS +
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

    private final static String CREATE_LESSON_TABLE = "CREATE TABLE IF NOT EXISTS " + TB_NAME_LESSON +
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

    private final static String CREATE_EXAM_TABLE = "CREATE TABLE IF NOT EXISTS "+ TB_NAME_EXAM +
            "(class_index Integer," +                    //课程索引
            "exam_index Integer," +                    //考试索引
            "name varchar(50)," +                    //考试名称
            "description varchar(500)," +                    //课时索引
            "start_time Integer," +                      //开始时间
            "end_time Integer," +                        //结束时间
            "locale varchar(50)," +                      //考试地点
            "exam_credits Integer)";                //考试能得到的积分

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(name, null);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_LESSON_TABLE);
        db.execSQL(CREATE_EXAM_TABLE);
        db.execSQL(CREATE_MYCLASS_STU_TABLE);
        db.execSQL(CREATE_MYCLASS_TEACHER_TABLE);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TB_NAME_CLASS);
        db.execSQL("DROP TABLE IF EXISTS" + TB_NAME_LESSON);
        db.execSQL("DROP TABLE IF EXISTS" + TB_NAME_EXAM);
        db.execSQL("DROP TABLE IF EXISTS" + TB_NAME_MY_CLASS_STU);
        db.execSQL("DROP TABLE IF EXISTS" + TB_NAME_MY_CLASS_TEACHER);
        onCreate(db);
        Log.d(TAG, "onUpgrade");
    }
}
