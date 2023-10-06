package com.ldx.blog.utils;

import org.junit.jupiter.api.Test;

/**
 * @author Uaena
 * @date 2023/6/14 19:37
 */

public class GenerateMDUtil {
    @Test
    public void test() {
        String str = "aa";
        System.out.println(str.concat(".md"));
//        String content ="aaa";
//        try {
//            Path path = Paths.get(System.getProperty("user.dir"), "file", "test","md");
//            Files.createDirectories(path.getParent());
//            try (FileWriter writer = new FileWriter(path.toFile(), true)) {
//                writer.write(content);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);

    }
}
