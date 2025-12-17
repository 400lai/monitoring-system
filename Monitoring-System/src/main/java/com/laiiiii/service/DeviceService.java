package com.laiiiii.service;

import com.laiiiii.domain.Device;

public interface DeviceService {

    /**
     * 根据ID获取设备详情
     */
    Device getDeviceById(Long id);
}