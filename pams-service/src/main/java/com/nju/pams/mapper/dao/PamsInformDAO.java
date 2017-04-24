package com.nju.pams.mapper.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nju.pams.model.constant.DatabaseConstant;
import com.nju.pams.model.system.PamsInform;
import com.nju.pams.util.annotation.DAOMapper;

@DAOMapper
public interface PamsInformDAO {
	public static final String TABLE = DatabaseConstant.T_PAMS_INFORM;

    public static final String COL_ALL = " inform_id, status, inform_type_id,"
    		+ " record_date, message, create_time, update_time ";
    
    /**
     * 根据informId查询通知
     * @param informId
     * @return
     */
    @Select(""
            + " SELECT "
            + COL_ALL
            + " FROM "
            + TABLE
            + " WHERE "
            + " inform_id = #{informId} "
            + "")
    public PamsInform getPamsInformByInformId(@Param("informId") Integer informId);
    
    /**
     * 获取最新的有效的公告
     * @return
     */
    @Select(""
            + " SELECT "
            + COL_ALL
            + " FROM "
            + TABLE
            + " WHERE "
            + " status = 0 "
            + " ORDER BY "
            + " record_date DESC "
            + " LIMIT 1 "
            + "")
    public PamsNotice getNewestValidPamsNotice();
    
    /**
     * 获取所有公告
     * @return
     */
    @Select(""
            + " SELECT "
            + COL_ALL
            + " FROM "
            + TABLE
            + "")
    public List<PamsNotice> getAllPamsNotice();
    
    /**
     * 插入公告，notice_id create_time update_time由数据库操作
     * @param pamsNotice
     */
    @Insert(""
    		+ " INSERT "
    		+ " INTO "
    		+ TABLE
    		+ " ( " + COL_ALL + " ) "
    		+ " VALUES "
    		+ " ( " 
    		+ " #{noticeId}, "
    		+ " #{status}, "
    		+ " #{recordDate}, "
    		+ " #{message}, "
    		+ " NOW(), "
    		+ " NOW() "
    		+ " ) "
    		+ "")
    @Options(useGeneratedKeys = true, keyColumn = "notice_id", keyProperty = "noticeId")
    public void insertPamsNotice(PamsNotice pamsNotice);
    
    /**
     * 更新公告内容
     * @param pamsNotice
     */
    @Update(""
    		+ " UPDATE "
    		+ TABLE
    		+ " SET "
    		+ " status = #{status}, "
    		+ " record_date = #{recordDate}, "
    		+ " message = #{message} "
    		+ " WHERE "
    		+ " notice_id = #{noticeId} "
    		+ "")
    public void updatePamsNotice(PamsNotice pamsNotice);
    
    /**
     * 删除公告
     * @param noticeId
     */
    @Delete(""
    		+ " DELETE "
    		+ " FROM "
    		+ TABLE
    		+ " WHERE "
    		+ " notice_id = #{noticeId} "
    		+ "")
    public void deletePamsNoticeByNoticeId(@Param("noticeId") Integer noticeId);
}
