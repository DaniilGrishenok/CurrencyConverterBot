package com.rhythmpoizon.currencyconverterbot.botapi.handlers;

import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class InfoCommandHandler {
//  @Value("${info.text}")
//   String infoText;
  String infoLink ="https://telegra.ph/Kak-ustanovit-prilozhenie-Poizon-i-oformit-dostavku-10-05";
  private TelegramBot telegramBot;


  @Autowired
  public void InfoCommandHandler(TelegramBot telegramBot) {
    this.telegramBot = telegramBot;
  }

  public void handleInfoCommand(long chatId)  {

    telegramBot.sendMessage(chatId, " INFO "+ infoLink );
  }
}
