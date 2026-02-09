package com.pfcbuy.gateway.controller;

import com.pfcbuy.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查Controller
 *
 * @author PfcBuy Team
 */
@Slf4j
@RestController
@RequestMapping("/actuator")
public class HealthController {

    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "gateway");
        data.put("timestamp", LocalDateTime.now());

        return Result.success(data);
    }
}
