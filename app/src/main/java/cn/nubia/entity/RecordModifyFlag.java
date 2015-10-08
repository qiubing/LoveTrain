package cn.nubia.entity;

import java.util.HashMap;
import java.util.Map;

import cn.nubia.db.SqliteHelper;

/**
 * Created by WJ on 2015/10/2.
 */
public class RecordModifyFlag {

    private RecordModifyFlag(){
        initRequestParams();
        initLastModifyMap();
    };

    private static RecordModifyFlag mRecordModifyFlag;

    public static synchronized  RecordModifyFlag getInstance(){
        if (mRecordModifyFlag == null)
            mRecordModifyFlag = new RecordModifyFlag();
        return mRecordModifyFlag;
    }

    public class RecordPair{
        long mLastCourseModifyTime;

        public long getLastCourseModifyTime() {
            return mLastCourseModifyTime;
        }

        public int getLastCourseIndex() {
            return mLastCourseIndex;
        }

        public long getLastLessonModifyTime() {
            return mLastLessonModifyTime;
        }

        public int getLastLessonIndex() {
            return mLastLessonIndex;
        }

        public long getLastExamModifyTime() {
            return mLastExamModifyTime;
        }

        public int getLastExamIndex() {
            return mLastExamIndex;
        }

        int mLastCourseIndex;
        long mLastLessonModifyTime;
        int mLastLessonIndex;
        long mLastExamModifyTime;
        int mLastExamIndex;
        RecordPair(long lastClassModifyTime, int lastClassIndex,long lastLessonModifyTime,int lastLessonIndex){
            mLastCourseModifyTime = lastClassModifyTime;
            mLastCourseIndex = lastClassIndex;
            mLastLessonModifyTime = lastLessonModifyTime;
            mLastLessonIndex = lastLessonIndex;
        }
        RecordPair(long lastExamModifyTime, int lastExamIndex){
            mLastExamModifyTime = lastExamModifyTime;
            mLastExamIndex = lastExamIndex;
        }
    };

    private long sLastCourseRecordModifyTimeForAll;     //记录最近修改时间用于增量更新
    private long slastLessonRecordModifyTimeForAll;
    private long slastExamRecordModifyTimeForAll;
    private int sLastLessonIndexForAll;
    private int sLastCourseIndexForAll;
    private int sLastExamIndexForAll;
    private long slastCourseRecordModifyTimeForStudent;
    private int sLastCourseIndexForStudent;
    private long slastLessonRecordModifyTimeForStudent;
    private int sLastLessonIndexForStudent;
    private long slastCourseRecordModifyTimeForTeacher;
    private int sLastCourseIndexForTeacher;
    private long slastLessonRecordModifyTimeForTeacher;
    private int sLastLessonIndexForTeacher;

    public void initRequestParams(){
        sLastCourseRecordModifyTimeForAll = 0;
        slastLessonRecordModifyTimeForAll = 0;
        slastExamRecordModifyTimeForAll = 0;
        sLastLessonIndexForAll = 0;
        sLastCourseIndexForAll = 0;
        sLastExamIndexForAll = 0;
        slastCourseRecordModifyTimeForStudent = 0;
        sLastCourseIndexForStudent = 0;
        slastLessonRecordModifyTimeForStudent = 0;
        sLastLessonIndexForStudent = 0;
        slastCourseRecordModifyTimeForTeacher = 0;
        sLastCourseIndexForTeacher = 0;
        slastLessonRecordModifyTimeForTeacher = 0;
        sLastLessonIndexForTeacher = 0;
        initLastModifyMap();
    }

    private static Map<String,RecordPair> sLastModifyMap;

    public Map<String,RecordPair> getRecordModifyMap(){
        return sLastModifyMap;
    }

    private void  initLastModifyMap(){
        sLastModifyMap = new HashMap<>();
        sLastModifyMap.put(SqliteHelper.TB_NAME_CLASS,
                new RecordPair(sLastCourseRecordModifyTimeForAll,sLastCourseIndexForAll,slastLessonRecordModifyTimeForAll,sLastLessonIndexForAll));
        sLastModifyMap.put(SqliteHelper.TB_NAME_MY_CLASS_STU,
                new RecordPair(slastCourseRecordModifyTimeForStudent,sLastCourseIndexForStudent,slastLessonRecordModifyTimeForStudent,sLastLessonIndexForStudent));
        sLastModifyMap.put(SqliteHelper.TB_NAME_MY_CLASS_TEACHER,
                new RecordPair(slastCourseRecordModifyTimeForTeacher,sLastCourseIndexForTeacher,slastLessonRecordModifyTimeForTeacher,sLastLessonIndexForTeacher));
        sLastModifyMap.put(SqliteHelper.TB_NAME_EXAM,
                new RecordPair(slastExamRecordModifyTimeForAll,sLastExamIndexForAll));
    }

    public void updateLastModifyMap(String tableName,long lastCourseRecordModifyTime,int lastCourseIndex,long lastLessonRecordModifyTime,int lastLessonIndex){
        if(sLastModifyMap.containsKey(tableName)){
            RecordPair pair = sLastModifyMap.get(tableName);
            pair.mLastCourseIndex = pair.mLastCourseIndex >  lastCourseIndex ? pair.mLastCourseIndex : lastCourseIndex;
            pair.mLastCourseModifyTime = pair.mLastCourseModifyTime > lastCourseRecordModifyTime ? pair.mLastCourseModifyTime : lastCourseRecordModifyTime;
            pair.mLastLessonIndex = pair.mLastLessonIndex > lastLessonIndex ? pair.mLastLessonIndex : lastLessonIndex;
            pair.mLastLessonModifyTime = pair.mLastLessonModifyTime>lastLessonRecordModifyTime ? pair.mLastLessonModifyTime : lastLessonRecordModifyTime;

            sLastModifyMap.put(tableName,pair);
        }
    }


    public void updateLastModifyMap(String tableName,long lastExamRecordModifyTime,int lastExamIndex){
        if(sLastModifyMap.containsKey(tableName)){
            RecordPair pair = sLastModifyMap.get(tableName);
            pair.mLastExamIndex = pair.mLastExamIndex > lastExamIndex ? pair.mLastExamIndex : lastExamIndex;
            pair.mLastExamModifyTime = pair.mLastExamModifyTime > lastExamRecordModifyTime ? pair.mLastExamModifyTime : lastExamRecordModifyTime;
        }
    }
}
