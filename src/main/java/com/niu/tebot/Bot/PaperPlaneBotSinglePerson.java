package com.niu.tebot.Bot;

import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * 此类设置单人机器人聊天消息
 */
public class PaperPlaneBotSinglePerson {



    protected void handleNonGroupMessage(Message message) {
        // 实现处理非群组消息的逻辑
        System.out.println("非群组消息内容: " + message.getText());
    }
}
