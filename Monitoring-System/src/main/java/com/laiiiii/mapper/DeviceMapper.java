package com.laiiiii.mapper;

import com.laiiiii.domain.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DeviceMapper {

    @Select("SELECT * FROM device WHERE id = #{id}")
    Device selectById(Long id);
}