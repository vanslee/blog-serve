package com.ldx.blog.components;

import com.ldx.blog.constants.RedisKeys;
import com.ldx.blog.pojo.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Uaena
 * @date 2023/7/30 23:27
 */
@SpringBootTest
@ActiveProfiles("test")
class RedisDaoTest {

    @Resource
    private RedisDao redisDao;

    @Test
    void addSetMember() {
        List<User> users = new ArrayList<>(3);
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        redisDao.lLPushAll(RedisKeys.TEST_KEY.concat("LIST:1"), users);

    }

    @Test
    void addSetMemberReal() {
    }

    @Test
    void lLPush() {
        List<User> users = new ArrayList<>(3);
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        Long aLong = redisDao.lLPushAll(RedisKeys.TEST_KEY.concat("List:1"), users);
        System.out.println(aLong);
    }
}