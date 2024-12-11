package com.oycm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController()
@RequestMapping("/redis")
public class RedisController {
    private final RedisTemplate<String,String> redisTemplate;

    @Autowired
    public RedisController(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("{key}")
    public String get(@PathVariable("key") String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @DeleteMapping("{key}")
    public String delete(@PathVariable("key") String key) {
        return Objects.requireNonNull(redisTemplate.delete(key)).toString();
    }

    @PostMapping("/{key}/{value}")
    public String add(@PathVariable("key") String key, @PathVariable("value") String value) {
        redisTemplate.opsForValue().set(key, value);
        return value;
    }
}
