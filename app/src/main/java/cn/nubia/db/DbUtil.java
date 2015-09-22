package cn.nubia.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.CourseItem;
import cn.nubia.entity.ExamItem;
import cn.nubia.entity.LessonItem;

/**
 * Created by WJ on 2015/9/6.
 */
public class DbUtil {
    private static final String TAG = "DbUtil";
    private static final String DB_NAME = "/loveTrain.db3";
    private static final int DB_VERSION = 1;
    private static DbUtil s_Db;
    private static  SQLiteDatabase db;
    private static SqliteHelper dbHelper;

    private  DbUtil(Context context) {
        dbHelper = new SqliteHelper(context, context.getFilesDir().toString() + DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DbUtil getInstance(Context context) {
        if (s_Db == null)
            s_Db = new DbUtil(context);
        return s_Db;
    }

    public void closeDb() {
        if (s_Db == null)
            return;
        if (db != null)
            db.close();
        if (dbHelper != null)
            dbHelper.close();
    }

    /**
     * for Debug
     */
    public void insertDataTest() {
        //插入10个课程
        for (int i = 0; i < 10; i++) {
            insertCourseItemTest(String.valueOf(i));
            //每个课程4个课时
            Log.e(TAG, "INSERT  data+");
            for (int j = 0; j < 4; j++) {
                insertLessonItemTest(String.valueOf(i));
            }
        }
    }

    /**
     * for Debug NO USE
     */
    private long insertCourseItemTest(String courseIndex) {
        ContentValues newValues = new ContentValues();
        newValues.put(CourseItem.COURSE_INDEX, courseIndex);
        newValues.put(CourseItem.NAME, "Java基础");
        newValues.put(CourseItem.HAS_EXAM, "0"); //1 有考试  0 无考试
        newValues.put(CourseItem.DESCRIPTION, "JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好，JAVA基础要学好");
        newValues.put(CourseItem.CREDITS, "20");
        newValues.put(CourseItem.SHARE_TYPE, "2");
        newValues.put(CourseItem.TYPE, "0");
        return db.insert(SqliteHelper.TB_NAME_CLASS, null, newValues);
    }

    /**
     * for Debug NO USE
     */
    private  long insertLessonItemTest(String courseIndex) {
        ContentValues newValues = new ContentValues();
        newValues.put(LessonItem.COURSE_INDEX, courseIndex);
        newValues.put(LessonItem.NAME, "Java基础一");
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

    public void updateCourseList(List<CourseItem> courseItemList) {
        for (CourseItem item : courseItemList)
            insertOrUpdateCourseItem(item);
    }

    private  long insertOrUpdateCourseItem(CourseItem courseItem) {
        Cursor cursor = db.query(SqliteHelper.TB_NAME_CLASS, new String[]{CourseItem.COURSE_INDEX}, CourseItem.COURSE_INDEX + "= ?",
                new String[]{String.valueOf(courseItem.getIndex())}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        Log.e(TAG, String.valueOf(courseItem));
        if (count != 0) /***数据库中有记录***/ {
            if (courseItem.getOperator().equals("update")) {
                return updateCourseItem(courseItem, SqliteHelper.TB_NAME_CLASS);
            } else  //删除
                return deleteCourseItem(courseItem, SqliteHelper.TB_NAME_CLASS);
        } else
            return insertCourseItem(courseItem, SqliteHelper.TB_NAME_CLASS);
    }

    public long insertCourseItem(CourseItem courseItem, String tableName) {
        ContentValues newValues = new ContentValues();
        newValues.put(CourseItem.COURSE_INDEX, courseItem.getIndex());
        newValues.put(CourseItem.NAME, courseItem.getName());
        newValues.put(CourseItem.HAS_EXAM, courseItem.hasExam()); //1 有考试  0 无考试
        newValues.put(CourseItem.DESCRIPTION, courseItem.getDescription());
//        newValues.put(CourseItem.JUDGE_SCORE, courseItem.getJudgeScore());

        newValues.put(CourseItem.CREDITS, courseItem.getCourseCredits());
        newValues.put(CourseItem.SHARE_TYPE, courseItem.getShareType());
        newValues.put(CourseItem.LESSONES_COUNT, courseItem.getLessones());
        newValues.put(CourseItem.STATUS, courseItem.getCourseStatus());
        newValues.put(CourseItem.TYPE, courseItem.getType());
        newValues.put(CourseItem.RECORD_MODIFY_TIME, courseItem.getRecordModifyTime());
        return db.insert(tableName, null, newValues);
    }

    public long updateCourseItem(CourseItem courseItem, String tableName) {
        ContentValues newValues = new ContentValues();
        newValues.put(CourseItem.COURSE_INDEX, courseItem.getIndex());
        newValues.put(CourseItem.NAME, courseItem.getName());
        newValues.put(CourseItem.HAS_EXAM, courseItem.hasExam()); //1 有考试  0 无考试
        newValues.put(CourseItem.DESCRIPTION, courseItem.getDescription());
//        newValues.put(CourseItem.JUDGE_SCORE, courseItem.getJudgeScore());
        newValues.put(CourseItem.CREDITS, courseItem.getCourseCredits());
        newValues.put(CourseItem.SHARE_TYPE, courseItem.getShareType());
        newValues.put(CourseItem.LESSONES_COUNT, courseItem.getLessones());
        newValues.put(CourseItem.STATUS, courseItem.getCourseStatus());
        newValues.put(CourseItem.TYPE, courseItem.getType());
        newValues.put(CourseItem.RECORD_MODIFY_TIME, courseItem.getRecordModifyTime());
        return db.update(tableName, newValues, CourseItem.COURSE_INDEX + "=" + courseItem.getIndex(), null);
    }

    public int deleteCourseItem(CourseItem lessonItem, String tableName) {
        //删除课时表
        int rows = db.delete(SqliteHelper.TB_NAME_LESSON, CourseItem.COURSE_INDEX + "=?",
                new String[]{String.valueOf(lessonItem.getIndex())});

        Log.e(TAG, "已删除课时表行数：" + rows);
        return db.delete(tableName, CourseItem.COURSE_INDEX + "=?",

                new String[]{String.valueOf(lessonItem.getIndex())});
    }

    public long insertLessonItem(LessonItem lessonItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(LessonItem.COURSE_INDEX, lessonItem.getCourseIndex());
        newValues.put(LessonItem.LESSON_INDEX, lessonItem.getIndex());
        Log.e("UpdateClassListHelper","LESSON_INDEX"+lessonItem.getIndex());
        newValues.put(LessonItem.NAME, lessonItem.getName());
        newValues.put(LessonItem.JUDGE_SCORE, lessonItem.getJudgeScore());
        newValues.put(LessonItem.CHECK_CREDITS, lessonItem.getCheckCredits());
        newValues.put(LessonItem.TEACHER_ID, lessonItem.getTeacherID());
        newValues.put(LessonItem.CHECK_USERS, lessonItem.getCheckUsers());
        newValues.put(LessonItem.TEACHER_NAME, lessonItem.getTeacherName());
        newValues.put(LessonItem.TEACHER_CREDITS, lessonItem.getTeacherCredits());
        newValues.put(LessonItem.LOCALE, lessonItem.getLocation());
        newValues.put(LessonItem.DESCRIPTION, lessonItem.getDescription());
        newValues.put(LessonItem.START_TIME, lessonItem.getStartTime());
        newValues.put(LessonItem.END_TIME, lessonItem.getEndTime());
        newValues.put(LessonItem.RECORD_MODIFY_TIME, lessonItem.getRecordModifyTime());
        return db.insert(SqliteHelper.TB_NAME_LESSON, null, newValues);
    }

    public long updateLessonItem(LessonItem lessonItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(LessonItem.COURSE_INDEX, lessonItem.getCourseIndex());
        newValues.put(LessonItem.LESSON_INDEX, lessonItem.getIndex());
        newValues.put(LessonItem.NAME, lessonItem.getName());
        newValues.put(LessonItem.JUDGE_SCORE, lessonItem.getJudgeScore());
        newValues.put(LessonItem.CHECK_CREDITS, lessonItem.getCheckCredits());
        newValues.put(LessonItem.TEACHER_ID, lessonItem.getTeacherID());
        newValues.put(LessonItem.CHECK_USERS, lessonItem.getCheckUsers());
        newValues.put(LessonItem.TEACHER_NAME, lessonItem.getTeacherName());
        newValues.put(LessonItem.TEACHER_CREDITS, lessonItem.getTeacherCredits());
        newValues.put(LessonItem.LOCALE, lessonItem.getLocation());
        newValues.put(LessonItem.DESCRIPTION, lessonItem.getDescription());
        newValues.put(LessonItem.START_TIME, lessonItem.getStartTime());
        newValues.put(LessonItem.END_TIME, lessonItem.getEndTime());
        newValues.put(LessonItem.RECORD_MODIFY_TIME, lessonItem.getRecordModifyTime());
        return db.update(SqliteHelper.TB_NAME_LESSON, newValues, LessonItem.LESSON_INDEX + "=" + lessonItem.getIndex(), null);
    }

    public int deleteLessonItem(LessonItem lessonItem) {
        return db.delete(SqliteHelper.TB_NAME_LESSON, LessonItem.LESSON_INDEX + " =? ",
                new String[]{String.valueOf(lessonItem.getIndex())});
    }

    public long insertExamItem(ExamItem examItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(ExamItem.COURSE_INDEX, examItem.getCourseIndex());
        newValues.put(ExamItem.EXAM_INDEX, examItem.getIndex());
        newValues.put(ExamItem.NAME, examItem.getName());
        newValues.put(ExamItem.EXAM_CREDITS, examItem.getExamCredits());
        newValues.put(ExamItem.LOCALE, examItem.getLocale());
        newValues.put(ExamItem.ENROLL_USERS, examItem.getErollUsers());
        newValues.put(ExamItem.DESCRIPTION, examItem.getDescription());
        newValues.put(ExamItem.START_TIME, examItem.getStartTime());
        newValues.put(ExamItem.END_TIME, examItem.getEndTime());
        return db.insert(SqliteHelper.TB_NAME_EXAM, null, newValues);
    }

    public long updateExamItem(ExamItem examItem) {
        ContentValues newValues = new ContentValues();
        newValues.put(ExamItem.COURSE_INDEX, examItem.getCourseIndex());
        newValues.put(ExamItem.EXAM_INDEX, examItem.getIndex());
        newValues.put(ExamItem.NAME, examItem.getName());
        newValues.put(ExamItem.EXAM_CREDITS, examItem.getExamCredits());
        newValues.put(ExamItem.LOCALE, examItem.getLocale());
        newValues.put(ExamItem.ENROLL_USERS, examItem.getErollUsers());
        newValues.put(ExamItem.DESCRIPTION, examItem.getDescription());
        newValues.put(ExamItem.START_TIME, examItem.getStartTime());
        newValues.put(ExamItem.END_TIME, examItem.getEndTime());
        return db.update(SqliteHelper.TB_NAME_EXAM, newValues, ExamItem.EXAM_INDEX + "=" + examItem.getIndex(), null);
    }

    public int deleteExamItem(ExamItem examItem) {
        return db.delete(SqliteHelper.TB_NAME_EXAM, ExamItem.EXAM_INDEX + " =? ",
                new String[]{String.valueOf(examItem.getIndex())});
    }

    public List<CourseItem> getCourseList(String tableName) {
        ArrayList<CourseItem> courseList = new ArrayList<CourseItem>();
        Cursor cursor = db.query(tableName, null, null, null, null,
                null, CourseItem.COURSE_INDEX + " DESC");
        if(cursor == null || cursor.isClosed()){
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            CourseItem courseItem = new CourseItem();
            courseItem.setIndex(cursor.getInt(cursor.getColumnIndex(CourseItem.COURSE_INDEX)));
            courseItem.setName(cursor.getString(cursor.getColumnIndex(CourseItem.NAME)));
            courseItem.setHasExam(cursor.getShort(cursor.getColumnIndex(CourseItem.HAS_EXAM)) != 0);
            courseItem.setCourseStatus(cursor.getShort(cursor.getColumnIndex(CourseItem.STATUS)));
            courseItem.setDescription(cursor.getString(cursor.getColumnIndex(CourseItem.DESCRIPTION)));
//            courseItem.setJudgeScore(cursor.getFloat(cursor.getColumnIndex(CourseItem.JUDGE_SCORE)));
            courseItem.setCourseCredits(cursor.getInt(cursor.getColumnIndex(CourseItem.CREDITS)));
            courseItem.setLessones(cursor.getShort(cursor.getColumnIndex(CourseItem.LESSONES_COUNT)));
            courseItem.setRecordModifyTime(cursor.getLong(cursor.getColumnIndex(CourseItem.RECORD_MODIFY_TIME)));
            courseItem.setShareType(cursor.getShort(cursor.getColumnIndex(CourseItem.SHARE_TYPE)));
            courseItem.setType(cursor.getString(cursor.getColumnIndex(CourseItem.TYPE)));
            List<LessonItem> lessonItemList = getLessonList(courseItem.getIndex());
            courseItem.setLessonList(lessonItemList);
            courseList.add(courseItem);
            cursor.moveToNext();
        }
        cursor.close();
        return courseList;
    }

    private  List<LessonItem> getLessonList(int CourseIndex) {
        ArrayList<LessonItem> lessonList = new ArrayList<>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME_LESSON, null, LessonItem.COURSE_INDEX + "=" + CourseIndex, null, null,
                null, LessonItem.LESSON_INDEX + " ASC");
        if(cursor == null || cursor.isClosed()){
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            LessonItem lessonItem = new LessonItem();
            lessonItem.setJudgeScore(cursor.getFloat(cursor.getColumnIndex(LessonItem.JUDGE_SCORE)));
            lessonItem.setIndex(cursor.getInt(cursor.getColumnIndex(LessonItem.LESSON_INDEX)));
            lessonItem.setCheckCredits(cursor.getInt(cursor.getColumnIndex(LessonItem.CHECK_CREDITS)));
            lessonItem.setEndTime(cursor.getLong(cursor.getColumnIndex(LessonItem.END_TIME)));
            lessonItem.setStartTime(cursor.getLong(cursor.getColumnIndex(LessonItem.START_TIME)));
            lessonItem.setName(cursor.getString(cursor.getColumnIndex(LessonItem.NAME)));
            lessonItem.setLocation(cursor.getString(cursor.getColumnIndex(LessonItem.LOCALE)));
            lessonItem.setTeacherCredits(cursor.getInt(cursor.getColumnIndex(LessonItem.TEACHER_CREDITS)));
            lessonItem.setTeacherID(cursor.getString(cursor.getColumnIndex(LessonItem.TEACHER_ID)));
            lessonItem.setTeacherName(cursor.getString(cursor.getColumnIndex(LessonItem.TEACHER_NAME)));
            lessonItem.setDescription(cursor.getString(cursor.getColumnIndex(LessonItem.DESCRIPTION)));
            lessonItem.setRecordModifyTime(cursor.getLong(cursor.getColumnIndex(LessonItem.RECORD_MODIFY_TIME)));
            lessonList.add(lessonItem);
            cursor.moveToNext();
        }
        cursor.close();
        return lessonList;
    }

    public List<ExamItem> getExamList() {
        ArrayList<ExamItem> examItemList = new ArrayList<ExamItem>();
        Cursor cursor = db.query(SqliteHelper.TB_NAME_EXAM, null, null, null, null,
                null, ExamItem.EXAM_INDEX + " DESC");
        if(cursor == null || cursor.isClosed()){
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            ExamItem examItem = new ExamItem();
            examItem.setIndex(cursor.getInt(cursor.getColumnIndex(ExamItem.EXAM_INDEX)));
            examItem.setCourseIndex(cursor.getInt(cursor.getColumnIndex(ExamItem.COURSE_INDEX)));
            examItem.setName(cursor.getString(cursor.getColumnIndex(ExamItem.NAME)));
            examItem.setDescription(cursor.getString(cursor.getColumnIndex(ExamItem.DESCRIPTION)));
            examItem.setExamCredits(cursor.getInt(cursor.getColumnIndex(ExamItem.EXAM_CREDITS)));
            examItem.setLocale(cursor.getString(cursor.getColumnIndex(ExamItem.LOCALE)));
            examItem.setEndTime(cursor.getLong(cursor.getColumnIndex(LessonItem.END_TIME)));
            examItem.setStartTime(cursor.getLong(cursor.getColumnIndex(LessonItem.START_TIME)));
            examItemList.add(examItem);
            cursor.moveToNext();
        }
        cursor.close();
        return examItemList;
    }
}
