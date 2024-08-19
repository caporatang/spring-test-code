package com.example.demo.common.infrastructure;

import com.example.demo.common.service.port.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * packageName : com.example.demo.common.infrastructure
 * fileName : SystemUuidHolder
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
@Component
public class SystemUuidHolder implements UuidHolder {
    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}