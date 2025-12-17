package com.laiiiii.mapper;

import com.laiiiii.domain.Emp;
import org.apache.ibatis.annotations.*;

/**
 * 员工信息
 */
@Mapper
public interface EmpMapper {



    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select id,username,name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);

    /**
     * 根据用户名查询员工信息
     */
    @Select("select id,username,name from emp where username = #{username}")
    Emp selectByUsername(String username);

    /**
     * 新增员工数据
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")   // 获取到生成的主键 -- 主键返回
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);
}
