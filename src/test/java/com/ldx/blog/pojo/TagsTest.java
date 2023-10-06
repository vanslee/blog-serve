package com.ldx.blog.pojo;

import com.ldx.blog.pojo.article.Tags;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Uaena
 * @date 2023/6/23 13:54
 */
public class TagsTest {
    @Test
    public void test() {
        Tags tag1 = new Tags(0,"11");
        Tags tag2 = new Tags(1,"222");
        Tags tag3 = new Tags(2,"333");
        List<Tags> list = new ArrayList<>(3);
        list.add(tag1);
        list.add(tag2);
        list.add(tag3);
        Tags tag4 = new Tags("111");
        if (list.contains(tag4)){
            System.out.println("true");
        }else {
            System.out.println("false");
        }
    }
}
