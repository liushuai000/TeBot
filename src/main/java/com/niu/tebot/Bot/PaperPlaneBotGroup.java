package com.niu.tebot.Bot;

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.groupadministration.PromoteChatMember;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 此类设置群组机器人聊天消息
 */
public class PaperPlaneBotGroup extends PaperPlaneBot{

    protected void handleGroupMessage(Message message) {
        PromoteChatMember promoteChatMember=null;
        String text = message.getText();
        Map<String, String> map = receiveMessage(message);
        // 实现处理群组消息的逻辑
        System.out.println("群组消息内容: " + message.getText());
        if (text.startsWith("设置为管理员")){
            promoteChatMember = this.promoteUserToAdmin(Long.parseLong(map.get("chatId")), Long.parseLong(map.get("userId")));
        }
        try {
            execute(promoteChatMember);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }



    /**
     * 设置用户为管理员
     * setCanChangeInfo: 允许用户更改群组信息。
     * setCanPostMessages: 允许用户发送消息。
     * setCanEditMessages: 允许用户编辑消息。
     * setCanDeleteMessages: 允许用户删除消息。
     * setCanRestrictMembers: 允许用户限制其他成员的权限。
     * setCanPinMessages: 允许用户固定消息。
     * setCanPromoteMembers: 允许用户提升其他成员的权限。
     * @param chatId
     * @param userId
     * @return
     */
    public PromoteChatMember promoteUserToAdmin(long chatId, long userId) {
        PromoteChatMember promoteChatMember = new PromoteChatMember();
        promoteChatMember.setChatId(chatId);
        promoteChatMember.setUserId(userId);
        //可以更改信息
        promoteChatMember.setCanChangeInformation(true);
        //可以发布消息
        promoteChatMember.setCanPostMessages(true);
        //设置可以编辑消息
        promoteChatMember.setCanEditMessages(true);
        //可以删除消息
        promoteChatMember.setCanDeleteMessages(true);
        //设置可以邀请用户
        promoteChatMember.setCanInviteUsers(true);
        //可以限制成员
        promoteChatMember.setCanRestrictMembers(true);
        //设置可以固定消息
        promoteChatMember.setCanPinMessages(true);
        //可以提升成员
        promoteChatMember.setCanPromoteMembers(true);
        return promoteChatMember;
    }
    /**
     * 监听群组消息，并在收到消息时提取用户信息。
     * @param message
     */
    private Map<String,String> receiveMessage(Message message){
        Map<String,String> map = new HashMap<>();
        if (message.getFrom()!=null) {
            long userId = message.getFrom().getId();
            String username = message.getFrom().getUserName();
            Long chatId = message.getChatId();
            System.out.println("User ID: " + userId+" Username: " + username+" Chat ID: " + chatId);
            map.put("userId",userId+"");
            map.put("username",username);
            map.put("chatId",chatId+"");
        }
        return map;
    }


    /**
     * 这个方法可以用来获取群组中所有管理员的列表，其中包括用户的 userId。
     * @param chatId
     */
    public void printChatAdministrators(long chatId) {
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
        getChatAdministrators.setChatId(chatId);
        try {
            List<ChatMember> chatMembers = execute(getChatAdministrators);
            for (ChatMember chatMember : chatMembers) {
                System.out.println("User ID: " + chatMember.getUser().getId());
                System.out.println("Username: " + chatMember.getUser().getUserName());
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
