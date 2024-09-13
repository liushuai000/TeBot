package com.niu.tebot.Bot;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
public class BotSetting {
    public String input;//用户输入的消息
    long chatId;
    SendMessage sendMessage;
    Chat chat;
    Message message;
    String chatType;

    public BotSetting(Update update) {
        this.input = update.getMessage().getText();//用户输入的消息
        this.chatId = update.getMessage().getChatId();
        this.sendMessage = new SendMessage();
        this.message = update.getMessage();
        this.chat = message.getChat();
        this.chatType = chat.getType();

        //String input = update.getMessage().getText();//用户输入的消息
        //            long chatId = update.getMessage().getChatId();
        //            SendMessage sendMessage = new SendMessage();
        //
        //            Message message = update.getMessage();
        //            Chat chat = message.getChat();
        //            String chatType = chat.getType();

    }
}
