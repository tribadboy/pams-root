package com.nju.pams.mapper.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nju.pams.model.PamsUser;
import com.nju.pams.model.constant.DatabaseConstant;
import com.nju.pams.util.annotation.DAOMapper;

@DAOMapper
public interface PamsUserDAO {
	public static final String TABLE = DatabaseConstant.T_PAMS_USER;

    public static final String COL_ALL = " user_id, username, password, status, phone,"
    		+ " mail, create_time, update_time ";
    
    /**
     * 根据userId查询用户
     * @param userId
     * @return
     */
    @Select(""
            + " SELECT "
            + COL_ALL
            + " FROM "
            + TABLE
            + " WHERE "
            + " user_id = #{userId} "
            + "")
    public PamsUser getPamsUserByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select(""
            + " SELECT "
            + COL_ALL
            + " FROM "
            + TABLE
            + " WHERE "
            + " username = #{username} "
            + "")
    public PamsUser getPamsUserByUsername(@Param("username") String username);
    
    /**
     * 获取所有用户
     * @return
     */
    @Select(""
            + " SELECT "
            + COL_ALL
            + " FROM "
            + TABLE
            + " ORDER BY "
            + " username ASC "
            + "")
    public List<PamsUser> getPamsUserList();
    
    /**
     * 模糊查询用户
     * @param key
     * @return
     */
    @Select(""
            + " SELECT "
            + COL_ALL
            + " FROM "
            + TABLE
            + " WHERE "
            + " username LIKE #{key} "
            + " ORDER BY "
            + " username ASC "
            + "")
    public List<PamsUser> getPamsUsersByKey(@Param("key") String key);
    
    /**
     * 插入用户，user_id create_time update_time由数据库操作
     * @param pamsUser
     */
    @Insert(""
    		+ " INSERT "
    		+ " INTO "
    		+ TABLE
    		+ " ( " + COL_ALL + " ) "
    		+ " VALUES "
    		+ " ( " 
    		+ " #{userId}, "
    		+ " #{username}, "
    		+ " #{password}, "
    		+ " #{status}, "
    		+ " #{phone}, "
    		+ " #{mail}, "
    		+ " NOW(), "
    		+ " NOW() "
    		+ " ) "
    		+ "")
    @Options(useGeneratedKeys = true, keyColumn = "user_id", keyProperty = "userId")
    public void insertPamsUser(PamsUser pamsUser);
    
    /**
     * 更新用户信息，根据用户userId和username
     * @param pamsUser
     */
    @Update(""
    		+ " UPDATE "
    		+ TABLE
    		+ " SET "
    		+ " password = #{password}, "
    		+ " status = #{status}, "
    		+ " phone = #{phone}, "
    		+ " mail = #{mail} "
    		+ " WHERE "
    		+ " user_id = #{userId} "
    		+ " AND "
    		+ " username = #{username} "
    		+ "")
    public void updatePamsUser(PamsUser pamsUser);
    
    /**
     * 暂时使用完全删除，之后考虑伪删除
     * @param username
     */
    @Delete(""
    		+ " DELETE "
    		+ " FROM "
    		+ TABLE
    		+ " WHERE "
    		+ " username = #{username} "
    		+ "")
    public void deletePamsUserByUsername(@Param("username") String username);
}
