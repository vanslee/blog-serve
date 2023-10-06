//package com.ldx.blog.sse;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author Uaena
// * @date 2023/5/28 18:45
// */
//public class SseEmitter {
//    private static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
//
//    /**
//     * 创建连接
//     */
//    public static SseEmitter connect(String userId) {
//        try {
//            // 设置超时时间，0表示不过期。默认30秒
//            SseEmitter sseEmitter = new SseEmitter(0L);
//            // 注册回调
//            sseEmitter.onCompletion(completionCallBack(userId));
//            sseEmitter.onError(errorCallBack(userId));
//            sseEmitter.onTimeout(timeoutCallBack(userId));
//            sseEmitterMap.put(userId, sseEmitter);
//            count.getAndIncrement();
//            return sseEmitter;
//        } catch (Exception e) {
//            log.info("创建新的sse连接异常，当前用户：{}", userId);
//        }
//        return null;
//    }
//
//    /**
//     * 给指定用户发送消息
//     */
//    public static void sendMessage(String userId, String message) {
//
//        if (sseEmitterMap.containsKey(userId)) {
//            try {
//                sseEmitterMap.get(userId).send(message);
//            } catch (IOException e) {
//                log.error("用户[{}]推送异常:{}", userId, e.getMessage());
//                removeUser(userId);
//            }
//        }
//    }
//}
