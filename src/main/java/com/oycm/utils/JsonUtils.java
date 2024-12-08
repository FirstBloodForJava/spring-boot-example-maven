package com.oycm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JsonUtils {

    private static final Log log = LogFactory.getLog(JsonUtils.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String objToString(Object obj) {
        try {
            return objectMapper.writerFor(obj.getClass()).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
