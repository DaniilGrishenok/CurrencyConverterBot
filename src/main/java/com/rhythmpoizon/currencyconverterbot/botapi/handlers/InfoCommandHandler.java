package com.rhythmpoizon.currencyconverterbot.botapi.handlers;

import com.rhythmpoizon.currencyconverterbot.service.TelegramBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class InfoCommandHandler {

  final private String INFO_LINK ="https://telegra.ph/Kak-ustanovit-prilozhenie-Poizon-i-oformit-dostavku-10-05";
  String message = "Добро пожаловать в мир POIZON! 🌟\n\n" +
          "Если вы не знаете, как оформить заказ, не волнуйтесь, мы готовы помочь вам в этом! 🛒💼\n\n" +
          "Просто следуйте нашему подробному гайду, и вы сможете оформить заказ всего в несколько шагов. 📝💻\n\n" +
          "Не стесняйтесь, вы всегда можете обратиться к нам за помощью! Мы готовы сделать ваш опыт с POIZON приятным и удобным. 🤝✨";
  String text = "💪Админ\n" +
          "<a href='https://t.me/ppooizj'> Поможет в оформлении заказа и ответит на вопросы</a>\n" +
          "🤖Бот\n" +
          "<a href='https://t.me/RhythmPoizonBot'> Поможет рассчитать цену и оформить заказ</a>\n\n" +
          "❗Ответы на часто задаваемые вопросы❗️\n" +
          "<a href='https://telegra.ph/Kak-ustanovit-prilozhenie-Poizon-i-oformit-dostavku-10-05'> - Как скачать Poizon и оформить заказ🛒💼</a>\n" +
          "<a href='https://telegra.ph/Informaciya-po-oplate-10-05'> - Информация по доставке </a>\n\n" +
          "<a href='https://t.me/Poizon0_0'> 🖼 Наш основной канал </a>\n" +
          "<a href='https://t.me/+H51WJTC2PHkzOWIy'> 🌟Отзывы </a>\n\n";
  private TelegramBot telegramBot;
  @Autowired
  public void InfoCommandHandler(TelegramBot telegramBot) {
    this.telegramBot = telegramBot;
  }

  public void handleDeliveryCommand(long chatId){
    telegramBot.sendMessage(chatId,"<a href='https://telegra.ph/Informaciya-po-oplate-10-05'>Информация по доставке </a>\n\n");
  }

  public void handleInfoCommand(long chatId)  {

    telegramBot.sendMessage(chatId,text );
  }
}
