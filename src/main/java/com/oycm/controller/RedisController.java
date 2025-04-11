package com.oycm.controller;

//@RestController()
//@RequestMapping("/redis")
//public class RedisController {
//    private final RedisTemplate<String,String> redisTemplate;
//
//    @Autowired
//    public RedisController(RedisTemplate<String,String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    @GetMapping("{key}")
//    @Cacheable(value = "key", key = "#key")
//    public String get(@PathVariable("key") String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    @DeleteMapping("{key}")
//    public String delete(@PathVariable("key") String key) {
//        return Objects.requireNonNull(redisTemplate.delete(key)).toString();
//    }
//
//    @PostMapping("/{key}/{value}")
//    public String add(@PathVariable("key") String key, @PathVariable("value") String value) {
//        redisTemplate.opsForValue().set(key, value);
//        return value;
//    }
//}
