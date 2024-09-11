package com.niu.tebot.Bot;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Map;


@Component
public class PaperPlaneBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String username;
    private double exchangeRate = 1.0; // 默认汇率
    private Map<Long, Double> userRates = new HashMap<>();//用来存储每个客户的汇率
    @Override
    public String getBotToken() {
        return botToken; // 您的 Bot Token
    }
    @Override
    public String getBotUsername() {
        return username; // 您的 Bot 用户名
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String input = update.getMessage().getText();//用户输入的消息
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            this.BusinessHandle(input, chatId, message);
            this.sendReply(message,chatId,message.getText());
        }
    }
    //业务处理
    private void BusinessHandle(String input, long chatId, SendMessage message){
        if (input.startsWith("设置汇率")) {
            String huilv = input.substring("设置汇率".length()).trim();
            if (!huilv.isEmpty()) {
                try {
                    exchangeRate = Double.parseDouble(huilv);
                    userRates.put(chatId, exchangeRate);
                    message.setText("汇率已设置为: " + exchangeRate);
                } catch (NumberFormatException e) {
                    message.setText("请输入有效的汇率。");
                }
            } else {
                message.setText("请使用命令格式: 设置汇率<数字>");
            }
        } else if (input.startsWith("金额")) {
            String[] parts = input.split(" ");
            if (parts.length == 2) {
                try {
                    double amount = Double.parseDouble(parts[1]);
                    double convertedAmount = amount * userRates.getOrDefault(chatId, exchangeRate);
                    message.setText("转换后的金额: " + convertedAmount);
                } catch (NumberFormatException e) {
                    message.setText("请输入有效的金额。");
                }
            } else {
                message.setText("请使用命令格式: 金额<金额>");
            }
        } else {
            message.setText("欢迎使用汇率计算器! 使用 设置汇率 <汇率> 来设置汇率，使用 金额 <金额> 来计算金额。");
        }
    }



    private void sendReply(SendMessage message,Long chatId, String replyText) {
        message.setChatId(chatId);
        message.setText(replyText);
        try {
            execute(message); // 发送消息
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}