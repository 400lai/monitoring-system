package com.laiiiii.service.impl;

import com.laiiiii.domain.Device;
import com.laiiiii.mapper.DeviceMapper;
import com.laiiiii.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Device getDeviceById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return deviceMapper.selectById(id);
    }
}