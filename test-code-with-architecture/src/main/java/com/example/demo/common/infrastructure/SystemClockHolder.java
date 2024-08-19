package com.example.demo.common.infrastructure;

import com.example.demo.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;

/**
 * packageName : com.example.demo.common.infrastructure
 * fileName : SystemClockHolder
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
@Component
public class SystemClockHolder implements ClockHolder {


    @Override
    public long millis() {
        return Clock.systemUTC().millis();
    }
}