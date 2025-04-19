package com.oycm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonUtils {

    private static final Log log = LogFactory.getLog(JsonUtils.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        // 忽略未知属性导致转json失败异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String objToString(Object obj) {

        try {
            return objectMapper.writerFor(obj.getClass()).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
