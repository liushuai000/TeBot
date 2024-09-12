package com.niu.tebot.Bot;

import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.Access;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
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
            ReplyKeyboardMarkup replyKeyboardMarkup = this.sendReplyKeyboard();
            InlineKeyboardMarkup inlineKeyboard = this.sendInlineKeyboard("按钮名称", "1");
            this.sendReply(message,chatId,message.getText(),replyKeyboardMarkup,inlineKeyboard);
        }
    }


    //业务处理 https://t.me/cgbllm/4119
    private void BusinessHandle(String input, long chatId, SendMessage message){
        if (input.startsWith("设置汇率")) {
            String huilv = input.substring("设置汇率".length()).trim();
            if (!huilv.isEmpty()) {
                try {
                    exchangeRate = Double.parseDouble(huilv);
                    userRates.put(chatId, exchangeRate);
                    message.setText("汇率已设置为: " + exchangeRate+
                    "                               " +
                            "金额为"+0+"                        " +
                            "                                  " +
                            "当前汇率                             " +
                            "                                " +
                            "                    111");
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



    private void sendReply(SendMessage message,Long chatId, String replyText,ReplyKeyboardMarkup replyKeyboardMarkup,
                           InlineKeyboardMarkup inlineKeyboard ) {
        message.setChatId(chatId);
        message.setText(replyText);
        message.setReplyMarkup(replyKeyboardMarkup);//是否在onUpdateReceived设置
        message.setReplyMarkup(inlineKeyboard);
        try {
            execute(message); // 发送消息
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup sendReplyKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true); // 可选：一旦用户选择了按钮，键盘会消失
        // 创建按钮行
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Button 1"));
        row1.add(new KeyboardButton("Button 2"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Help"));
        // 将按钮行添加到键盘列表中
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        // 设置键盘
        replyKeyboardMarkup.setKeyboard(keyboard);
       return replyKeyboardMarkup;
    }

    /**
     * 发送内嵌键盘
     * @param buttonTextName 按钮名称
     */
    private InlineKeyboardMarkup sendInlineKeyboard(String buttonTextName,String callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setCallbackData(callbackData);
        inlineKeyboardButton1.setText(buttonTextName);
        inlineKeyboardButton1.setUrl("http://www.baidu.com");
        rowInline1.add(inlineKeyboardButton1);
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setCallbackData("2");
        inlineKeyboardButton2.setText("2");
        rowInline1.add(inlineKeyboardButton2);
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setCallbackData("3");
        inlineKeyboardButton3.setText("3");
        rowInline2.add(inlineKeyboardButton3);
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }
}