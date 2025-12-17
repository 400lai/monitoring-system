package com.laiiiii.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Device {
    private Long id;
    private String name;
    private String status; // 如: ONLINE, OFFLINE, MAINTENANCE
    private LocalDateTime lastUpdateTime;
    private String location; // 可选：设备位置
    private String model;    // 可选：型号
}