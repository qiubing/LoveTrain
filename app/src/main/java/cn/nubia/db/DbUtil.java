package cn.nubia.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.nubia.entity.Constant;
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

    public void dropDatabase(){
        Log.e("wj","dropDatabase");
        dbHelper.onDropDatabase(db);
    }

    public void updateCourseList(List<CourseItem> courseItemList) {
        for (CourseItem item : courseItemList){
            Log.e("wj getOperator",item.getOperator());
            insertOrUpdateCourseItem(item);
        }
    }

    private  long insertOrUpdateCourseItem(CourseItem courseItem) {
        Cursor cursor = db.query(SqliteHelper.TB_NAME_CLASS, new String[]{CourseItem.COURSE_INDEX}, CourseItem.COURSE_INDEX + "= ?",
                new String[]{String.valueOf(courseItem.getIndex())}, null, null, null);
        if(cursor == null || cursor.isClosed()){
            return -1;
        }
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
        newValues.put(CourseItem.OPERATOR, courseItem.getOperator());
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
        newValues.put(CourseItem.OPERATOR, courseItem.getOperator());
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
        Log.e("wj", "deleteCourseItem");
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
        newValues.put(LessonItem.NAME, lessonItem.getName());
        newValues.put(LessonItem.OPERATOR, lessonItem.getOperator());
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
        newValues.put(LessonItem.OPERATOR, lessonItem.getOperator());
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
        newValues.put(ExamItem.OPERATOR, examItem.getOperator());
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
        newValues.put(ExamItem.OPERATOR, examItem.getOperator());
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
            courseItem.setOperator(cursor.getString(cursor.getColumnIndex(CourseItem.OPERATOR)));
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

            /**更新课时索引 升序排列**/
            if(lessonItemList.size()>0){
                LessonItem lastLessonItem = lessonItemList.get(lessonItemList.size()-1);
                Constant.sLastLessonIndex = lastLessonItem.getIndex() > Constant.sLastLessonIndex
                        ?lastLessonItem.getIndex():Constant.sLastLessonIndex;
            }
            /**更新课程修改时间**/
            Constant.sLastCourseRecordModifyTime = courseItem.getRecordModifyTime() > Constant.sLastCourseRecordModifyTime
                    ?courseItem.getRecordModifyTime():Constant.sLastCourseRecordModifyTime;

            courseItem.setLessonList(lessonItemList);
            courseList.add(courseItem);
            cursor.moveToNext();
        }
        /**更新课程索引 降序排列**/
        if(courseList.size() > 0){
            Constant.sLastCourseIndex = courseList.get(0).getIndex();
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
            lessonItem.setOperator(cursor.getString(cursor.getColumnIndex(LessonItem.OPERATOR)));
            lessonItem.setCheckCredits(cursor.getInt(cursor.getColumnIndex(LessonItem.CHECK_CREDITS)));
            lessonItem.setCheckUsers(cursor.getInt(cursor.getColumnIndex(LessonItem.CHECK_USERS)));
            lessonItem.setEndTime(cursor.getLong(cursor.getColumnIndex(LessonItem.END_TIME)));
            lessonItem.setStartTime(cursor.getLong(cursor.getColumnIndex(LessonItem.START_TIME)));
            lessonItem.setName(cursor.getString(cursor.getColumnIndex(LessonItem.NAME)));
            lessonItem.setLocation(cursor.getString(cursor.getColumnIndex(LessonItem.LOCALE)));
            lessonItem.setTeacherCredits(cursor.getInt(cursor.getColumnIndex(LessonItem.TEACHER_CREDITS)));
            lessonItem.setTeacherID(cursor.getString(cursor.getColumnIndex(LessonItem.TEACHER_ID)));
            lessonItem.setTeacherName(cursor.getString(cursor.getColumnIndex(LessonItem.TEACHER_NAME)));
            lessonItem.setDescription(cursor.getString(cursor.getColumnIndex(LessonItem.DESCRIPTION)));
            lessonItem.setRecordModifyTime(cursor.getLong(cursor.getColumnIndex(LessonItem.RECORD_MODIFY_TIME)));

            /**更新课时修改时间和索引**/
            Constant.slastLessonRecordModifyTime = lessonItem.getRecordModifyTime() > Constant.slastLessonRecordModifyTime
                    ?lessonItem.getRecordModifyTime():Constant.slastLessonRecordModifyTime;

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
            examItem.setErollUsers(cursor.getInt(cursor.getColumnIndex(ExamItem.ENROLL_USERS)));
            examItem.setDescription(cursor.getString(cursor.getColumnIndex(ExamItem.DESCRIPTION)));
            examItem.setOperator(cursor.getString(cursor.getColumnIndex(ExamItem.OPERATOR)));
            examItem.setExamCredits(cursor.getInt(cursor.getColumnIndex(ExamItem.EXAM_CREDITS)));
            examItem.setLocale(cursor.getString(cursor.getColumnIndex(ExamItem.LOCALE)));
            examItem.setEndTime(cursor.getLong(cursor.getColumnIndex(LessonItem.END_TIME)));
            examItem.setStartTime(cursor.getLong(cursor.getColumnIndex(LessonItem.START_TIME)));
            /**更新考试记录最近时间**/
            Constant.slastExamRecordModifyTime = examItem.getRecordModifyTime() > Constant.slastExamRecordModifyTime
                    ?examItem.getRecordModifyTime() : Constant.slastExamRecordModifyTime;

            examItemList.add(examItem);
            cursor.moveToNext();
        }
        cursor.close();

        /**更新课程索引 降序排列**/
        if(examItemList.size() > 0){
            Constant.sLastExamIndex = examItemList.get(0).getIndex();
        }
        return examItemList;
    }


    public long getParamFromDB(String tableName,String columnStr,boolean isDESC) {
        String sortType = isDESC?"DESC":"ASC";
        Cursor cursor = db.query(tableName, new String[]{columnStr}, null, null, null,
                null, columnStr + "  " + sortType);
        if(cursor == null || cursor.isClosed()){
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndex(columnStr));
    }
}
