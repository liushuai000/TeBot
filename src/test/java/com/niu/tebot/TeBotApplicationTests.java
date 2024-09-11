package com.niu.tebot;

import com.niu.tebot.Bot.PaperPlaneBot;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@SpringBootTest
class TeBotApplicationTests {

    private static final String BOT_TOKEN = "7203617407:AAGq9fe9aZSEanFkOa3oa219bEnJI_4RKY4";  // 替换为你的机器人令牌
    private static final String BASE_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/";
    private OkHttpClient client;


    TeBotApplicationTests(OkHttpClient client) {
        this.client = client;
    }

    private void getUpdates() throws IOException, JSONException {
        String url = BASE_URL + "getUpdates";
        //System.err.println(url);
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseData = response.body().string();
            JSONObject json = new JSONObject(responseData);
            JSONArray messages = json.getJSONArray("result");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i).getJSONObject("message");
                String chatId = message.getJSONObject("chat").get("id") + "";
                String userMessage = message.getString("text");
                // 这里可以根据具体的用户消息做处理
//                String replyMessage = "你说的是: " + userMessage; // 自动回复的内容
                String replyMessage = "";
                if (userMessage.equals("设置汇率")){

                }else {
                    //没有这个选项
                    replyMessage = "There is no such option";
                }
                sendMessage(chatId, replyMessage);
            }
        }
    }
    private void sendMessage(String chatId, String text) throws IOException {
        String url = BASE_URL + "sendMessage?chat_id=" + chatId + "&text=" + text;
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).execute();
    }
    @Test
    void contextLoads() {
        client = new OkHttpClient();
        try {
            while (true) {
                this.getUpdates();
                Thread.sleep(5000); // 每隔5秒获取一次更新
            }
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

}
