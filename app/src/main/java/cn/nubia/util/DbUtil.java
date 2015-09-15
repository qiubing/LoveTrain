package cn.nubia.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.CourseItem;
import cn.nubia.entity.LessonItem;

/**
 * Created by WJ on 2015/9/6.
 */
public class DbUtil {
    private static  String TAG = "DbUtil";
    private static String DB_NAME = "/loveTrain.db3";
    private static int DB_VERSION = 1;
    private static DbUtil s_Db;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;
    private DbUtil(Context context) {
        dbHelper = new SqliteHelper(context, context.getFilesDir().toString() +DB_NAME, null, DB_VERSION );
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DbUtil getInstance(Context context){
        if (s_Db == null)
            s_Db = new DbUtil(context);
        return s_Db;
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }
/**
 * for Debug
 */
    public void insertDataTest(){
        //插入10个课程
        for(int i = 0; i<10;i++){
            insertCourseItemTest(String.valueOf(i));
            //每个课程4个课时
            Log.e(TAG,"INSERT  data+");
            for (int j = 0; j<4;j++){
                insertLessonItemTest(String.valueOf(i));
            }
        }
    }

    /**
     * for Debug
     */
    public long insertCourseItemTest(String courseIndex) {
        ContentValues newValues = new ContentValues();
        newValues.put(CourseItem.INDEX,courseIndex);
        newValues.put(CourseItem.NAME,"Java基础");
        newValues.put(CourseItem.HASEXAM, "0"); //1 有考试  0 无考试
        newValues.put(CourseItem.DESCRIPTION, "JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好");
        newValues.put(CourseItem.CREDITS, "20");
        newValues.put(CourseItem.SHARETYPE, "2");
        newValues.put(CourseItem.TYPE, "0");
        return db.insert(SqliteHelper.TB_NAME_CLASS, null, newValues);
    }

    /**
     * for Debug
     */
    public long insertLessonItemTest(String courseIndex) {
        ContentValues newValues = new ContentValues();
        newValues.put(LessonItem.COURSE_INDEX,courseIndex);
        newValues.put(LessonItem.NAME,"Java基础一");
        newValues.put(LessonItem.JUDGE_SCORE, "90.5"); //1 有考试  0 无考试
        newValues.put(LessonItem.CHECK_CREDITS, "10");
        newValues.put(LessonItem.TEACHER_CREDITS, "30");
        newValues.put(LessonItem.TEACHER_NAME, "张三");
        newValues.put(LessonItem.LOCALE, "C-2");
        newValues.put(LessonItem.DESCRIPTION, "第一课");
        newValues.put(LessonItem.START_TIME, "7月8号9点10分");
        newValues.put(LessonItem.END_TIME, "7月8号10点10分");
        return db.insert(SqliteHelper.TB_NAME_LESSON, null, newValues);
    }

    public long insertCourseItem(CourseItem courseItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(CourseItem.INDEX,courseItem.getIndex());
        newValues.put(CourseItem.NAME,courseItem.getName());
        newValues.put(CourseItem.HASEXAM, courseItem.hasExam()); //1 有考试  0 无考试
        newValues.put(CourseItem.DESCRIPTION,courseItem.getDescription());
//        newValues.put(CourseItem.JUDGE_SCORE, courseItem.getJudgeScore());
        newValues.put(CourseItem.CREDITS, courseItem.getCourseCredits());
        newValues.put(CourseItem.SHARETYPE, courseItem.getShareType());
        newValues.put(CourseItem.TYPE,courseItem.getType());
        return db.insert(SqliteHelper.TB_NAME_CLASS, null, newValues);
    }

    public long updateCourseItem(CourseItem courseItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(CourseItem.INDEX,courseItem.getIndex());
        newValues.put(CourseItem.NAME,courseItem.getName());
        newValues.put(CourseItem.HASEXAM, courseItem.hasExam()); //1 有考试  0 无考试
        newValues.put(CourseItem.DESCRIPTION,courseItem.getDescription());
//        newValues.put(CourseItem.JUDGE_SCORE, courseItem.getJudgeScore());
        newValues.put(CourseItem.CREDITS, courseItem.getCourseCredits());
        newValues.put(CourseItem.SHARETYPE, courseItem.getShareType());
        newValues.put(CourseItem.TYPE,courseItem.getType());
        return db.update(SqliteHelper.TB_NAME_CLASS, newValues, CourseItem.INDEX + "=" + courseItem.getIndex(), null);
    }

    public int deleteCourseItem(CourseItem lessonItem) {
        //删除课时表
        int rows = db.delete(SqliteHelper.TB_NAME_LESSON,CourseItem.INDEX + "=?",
                new String[]{String.valueOf(lessonItem.getIndex())});
        Log.e(TAG,"已删除课时表行数："+rows);
        return db.delete(SqliteHelper.TB_NAME_CLASS, CourseItem.INDEX + "=?",
                new String[]{String.valueOf(lessonItem.getIndex())});
    }

    public long insertLessonItem(LessonItem lessonItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(LessonItem.COURSE_INDEX,lessonItem.getCourseIndex());
        newValues.put(LessonItem.INDEX,lessonItem.getIndex());
        newValues.put(LessonItem.NAME,lessonItem.getName());
        newValues.put(LessonItem.JUDGE_SCORE, lessonItem.getJudgeScore());
        newValues.put(LessonItem.CHECK_CREDITS, lessonItem.getCheckCredits());
        newValues.put(LessonItem.TEACHER_ID, lessonItem.getTeacherID());
        newValues.put(LessonItem.TEACHER_NAME, lessonItem.getTeacherName());
        newValues.put(LessonItem.TEACHER_CREDITS, lessonItem.getTeacherCredits());
        newValues.put(LessonItem.LOCALE, lessonItem.getLocation());
        newValues.put(LessonItem.DESCRIPTION,lessonItem.getDescription());
        newValues.put(LessonItem.START_TIME, lessonItem.getStartTime());
        newValues.put(LessonItem.END_TIME, lessonItem.getEndTime());
        return db.insert(SqliteHelper.TB_NAME_LESSON, null, newValues);
    }

    public long updateLessonItem(LessonItem lessonItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(LessonItem.COURSE_INDEX,lessonItem.getCourseIndex());
        newValues.put(LessonItem.INDEX,lessonItem.getIndex());
        newValues.put(LessonItem.NAME,lessonItem.getName());
        newValues.put(LessonItem.JUDGE_SCORE, lessonItem.getJudgeScore());
        newValues.put(LessonItem.TEACHER_ID, lessonItem.getTeacherID());
        newValues.put(LessonItem.TEACHER_NAME, lessonItem.getTeacherName());
        newValues.put(LessonItem.CHECK_CREDITS, lessonItem.getCheckCredits());
        newValues.put(LessonItem.TEACHER_CREDITS, lessonItem.getTeacherCredits());
        newValues.put(LessonItem.LOCALE, lessonItem.getLocation());
        newValues.put(LessonItem.DESCRIPTION,lessonItem.getDescription());
        newValues.put(LessonItem.START_TIME, lessonItem.getStartTime());
        newValues.put(LessonItem.END_TIME, lessonItem.getEndTime());
        return db.update(SqliteHelper.TB_NAME_LESSON, newValues, LessonItem.INDEX + "=" + lessonItem.getIndex(), null);
    }

    public int deleteLessonItem(LessonItem lessonItem) {
        return db.delete(SqliteHelper.TB_NAME_LESSON,LessonItem.INDEX + "=?",
                new String[]{String.valueOf(lessonItem.getIndex())});
    }

    public List<CourseItem> getCourseList(){
        ArrayList<CourseItem> courseList = new ArrayList<CourseItem>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME_CLASS, null, null, null, null,
                null, CourseItem.INDEX + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            CourseItem courseItem = new CourseItem();
            courseItem.setIndex(cursor.getInt(cursor.getColumnIndex(CourseItem.INDEX)));
            courseItem.setName(cursor.getString(cursor.getColumnIndex(CourseItem.NAME)));
            courseItem.setHasExam(cursor.getShort(cursor.getColumnIndex(CourseItem.HASEXAM)));
            courseItem.setCourseStatus(cursor.getShort(cursor.getColumnIndex(CourseItem.STATUS)));
            courseItem.setDescription(cursor.getString(cursor.getColumnIndex(CourseItem.DESCRIPTION)));
//            courseItem.setJudgeScore(cursor.getFloat(cursor.getColumnIndex(CourseItem.JUDGE_SCORE)));
            courseItem.setCourseCredits(cursor.getInt(cursor.getColumnIndex(CourseItem.CREDITS)));
            courseItem.setShareType(cursor.getShort(cursor.getColumnIndex(CourseItem.SHARETYPE)));
            courseItem.setType(cursor.getString(cursor.getColumnIndex(CourseItem.TYPE)));
            courseList.add(courseItem);
            cursor.moveToNext();
        }
        cursor.close();
        return courseList;
    }

    public List<LessonItem> getLessionList(int CourseIndex ){
        ArrayList<LessonItem> lessonList = new ArrayList<>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME_LESSON, null,LessonItem.COURSE_INDEX + "=" + CourseIndex, null, null,
                null, LessonItem.INDEX + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            LessonItem lessonItem = new LessonItem();
            lessonItem.setJudgeScore(cursor.getFloat(cursor.getColumnIndex(LessonItem.JUDGE_SCORE)));
            lessonItem.setCheckCredits(cursor.getInt(cursor.getColumnIndex(LessonItem.CHECK_CREDITS)));
//            lessonItem.setEndTime(cursor.getString(cursor.getColumnIndex(LessonItem.END_TIME)));
//            lessonItem.setStartTime(cursor.getString(cursor.getColumnIndex(LessonItem.START_TIME)));
            lessonItem.setName(cursor.getString(cursor.getColumnIndex(LessonItem.NAME)));
            lessonItem.setLocation(cursor.getString(cursor.getColumnIndex(LessonItem.LOCALE)));
            lessonItem.setTeacherCredits(cursor.getInt(cursor.getColumnIndex(LessonItem.TEACHER_CREDITS)));
            lessonItem.setTeacherID(cursor.getString(cursor.getColumnIndex(LessonItem.TEACHER_ID)));
            lessonItem.setTeacherName(cursor.getString(cursor.getColumnIndex(LessonItem.TEACHER_NAME)));
            lessonItem.setDescription(cursor.getString(cursor.getColumnIndex(LessonItem.DESCRIPTION)));
            lessonList.add(lessonItem);
            cursor.moveToNext();
        }
        cursor.close();
        return lessonList;
    }


}
