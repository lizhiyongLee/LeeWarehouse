package com.ils.modules.mes.config.aspect;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 对hutool的StopWatch进行封装
 *
 * @author niushuai
 * @date 2021/11/23 9:31:45
 */
@Slf4j
public class StopWatchUtil {

    private static final Map<String, StopWatch> taskMap = new HashMap<>(16);

    public static void start(String name) {
        log.info("{} start watch", name);
        StopWatch watch = new StopWatch();
        watch.start(name);
        taskMap.put(name, watch);
    }

    public static void stop(String name) {
        StopWatch watch = taskMap.get(name);
        Optional.ofNullable(watch).orElseThrow(() -> new RuntimeException(name + "不存在"));
        if (watch.isRunning()) {
            print(name, watch);
        }
        taskMap.remove(name);
    }

    private static void print(String name, StopWatch watch) {
        watch.stop();
        log.info("{} stop watch total use: {}s", name, watch.getTotalTimeSeconds());
    }
}
