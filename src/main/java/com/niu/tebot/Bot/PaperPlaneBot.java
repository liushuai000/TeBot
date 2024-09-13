package com.niu.tebot.Bot;

import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.groupadministration.PromoteChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.Access;
import java.util.*;

@Accessors(chain = true)
@Component
public class PaperPlaneBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String username;
    private double exchangeRate = 1.0; // 默认汇率
    private Map<Long, Double> userRates = new HashMap<>();//用来存储每个客户的汇率
    //个人和群组应该是都需要按钮
    private static final PaperPlaneBotButton paperPlaneBotButton = new PaperPlaneBotButton();
    private static final ReplyKeyboardMarkup replyKeyboardMarkup = paperPlaneBotButton.sendReplyKeyboard();
    private static final InlineKeyboardMarkup inlineKeyboard = paperPlaneBotButton.sendInlineKeyboard("按钮名称", "1","www.telegram.org");
    //------------------------------------------------以下是消息类分类----------------------------------------------------
    private static final PaperPlaneBotGroup botGroup = new PaperPlaneBotGroup();//群组的
    private static final PaperPlaneBotSinglePerson singlePerson = new PaperPlaneBotSinglePerson();//个人的

    static {
        //这个是为了多次生成inlineKeyboard  内部按钮 封装下 需要添加按钮在此处添加就可以
        Map<String,String> buttonTextNameMap = new HashMap<>();//结构: <按钮名称，<按钮callBackData,url>>
        Map<String,String> callBackDataMap = new HashMap<>();//注：所有的key必须一致
        Map<String,String> urlMap = new HashMap<>();

        buttonTextNameMap.put("callBackData1","www.telegram.org");
        buttonTextNameMap.put("callBackData2","www.baidu.com");
        buttonTextNameMap.put("callBackData3","https://blog.csdn.net");

//        if (!maps.isEmpty()){
//            for(Map.Entry<String,String> entry : maps.entrySet()){
//                paperPlaneBotButton.sendInlineKeyboard(entry.getKey(),entry.getValue());
//            }
//        }

    }

    @Override
    public String getBotToken() {
        return botToken; // 您的 Bot Token
    }
    @Override
    public String getBotUsername() {
        return username; // 您的 Bot 用户名
    }

    /**
     * 发送图片：使用 SendPhoto 类。
     * 发送视频：使用 SendVideo 类。
     * 发送文件：使用 SendDocument 类。
     * 发送音频：使用 SendAudio 类。
     * @param update
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            //基础信息都封装到BotSetting
            BotSetting botSetting = new BotSetting(update);
            // 判断是否是群组消息 群组聊天类型：group：普通群组 supergroup：超级群组（高级群组）私人聊天类型：private：个人聊天
            if ("group".equals(botSetting.getChatType()) || "supergroup".equals(botSetting.getChatType())) {
                System.out.println("这是一个来自群组的消息。");
                // 处理群组消息
                botGroup.handleGroupMessage(botSetting.getMessage());
            } else {
                System.out.println("这是一个非群组消息。");
                // 处理非群组消息
                singlePerson.handleNonGroupMessage(botSetting.getMessage());
            }
            this.BusinessHandle(botSetting.getInput(), botSetting.getChatId(), botSetting.getSendMessage());
            if (botSetting.getMessage().getText().startsWith("设置汇率")||botSetting.getMessage().getText().startsWith("金额")){
                this.sendInlineKeyboardReply(botSetting.getSendMessage(),botSetting.getChatId(),botSetting.getMessage().getText(),inlineKeyboard);
            }else {
                this.sendReply(botSetting.getSendMessage(),botSetting.getChatId(),botSetting.getMessage().getText(),replyKeyboardMarkup);
            }


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
    // 发送带普通键盘的消息
    private void sendReply(SendMessage message,Long chatId, String replyText,ReplyKeyboardMarkup replyKeyboardMarkup) {
        message.setChatId(chatId);
//        message.setText(replyText);
        message.setReplyMarkup(replyKeyboardMarkup);//是否在onUpdateReceived设置
        try {
            execute(message); // 发送消息
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置内联键盘选项
     * @param message
     * @param chatId
     * @param inlineKeyboard
     */
    private void sendInlineKeyboardReply(SendMessage message,Long chatId, String replyText,InlineKeyboardMarkup inlineKeyboard ) {
        message.setChatId(chatId);
//        message.setText(replyText);
        message.setReplyMarkup(inlineKeyboard);//内嵌按钮
        try {
            execute(message); // 发送消息
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}