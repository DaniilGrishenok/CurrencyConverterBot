package com.rhythmpoizon.currencyconverterbot.service;

import com.rhythmpoizon.currencyconverterbot.botapi.handlers.InfoCommandHandler;
import com.rhythmpoizon.currencyconverterbot.botapi.handlers.ManagerCommandHandler;
import com.rhythmpoizon.currencyconverterbot.botapi.handlers.PriceCommandHandler;
import com.rhythmpoizon.currencyconverterbot.botapi.handlers.CurrencyCommandHendler;
import com.rhythmpoizon.currencyconverterbot.config.BotConfig;
import com.rhythmpoizon.currencyconverterbot.model.User;
import com.rhythmpoizon.currencyconverterbot.model.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.*;



@Slf4j
@Component
public class TelegramBot  extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    @Lazy
    @Autowired
    PriceCommandHandler priceCommandHandler;

    @Lazy
    @Autowired
    CurrencyCommandHendler currencyCommandHendler;

    @Lazy
    @Autowired
    InfoCommandHandler infoCommandHandler;
    @Lazy
    @Autowired
    ManagerCommandHandler managerCommandHandler;
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    private Map<Long, BotState> chatStates = new HashMap<>();

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start") || messageText.equals("hello") || messageText.equals("привет")) {
                handleStartCommand(update.getMessage());

            } else if (messageText.equals("/currency") || messageText.equals("Курс Юаня")|| messageText.equals("курс")) {
                currencyCommandHendler.handleCurrencyCommand(chatId);

            } else if (messageText.equals("/info")) {
                infoCommandHandler.handleInfoCommand(chatId);

            } else if (messageText.equals("/manager")|| messageText.equals("заказ") || messageText.equals("Сделать заказ") ) {
                managerCommandHandler.handleManagerCommand(chatId);

            } else if (messageText.equals("/price")||messageText.equals("Калькулятор цены")) {
                if (chatStates.get(chatId) == BotState.WAITING_FOR_PRICE) {
                    // Пользователь уже находится в режиме ожидания цены, и мы ожидаем от него ввода цены.
                    try {
                        double priceInYuan = Double.parseDouble(messageText);
                        priceCommandHandler.handlePriceCommand(chatId, priceInYuan);
                        // После обработки цены возвращаем бота в начальное состояние
                        chatStates.remove(chatId);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Пожалуйста, введите корректную числовую цену в юанях.");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Если пользователь ввел команду /price, но бот не ожидает цену, переводим его в режим ожидания цены
                    chatStates.put(chatId, BotState.WAITING_FOR_PRICE);
                    sendMessage(chatId, "Введите цену на товар в юанях");
                }
            } else {
                if (chatStates.containsKey(chatId) && chatStates.get(chatId) == BotState.WAITING_FOR_PRICE) {
                    // Если бот находится в режиме ожидания цены, обрабатываем ввод пользователя как цену
                    try {
                        double priceInYuan = Double.parseDouble(messageText);
                        priceCommandHandler.handlePriceCommand(chatId, priceInYuan);
                        chatStates.remove(chatId);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Извините я вас не понял( \n" +
                                " Посмотрите пожалуйста список команд и выберете нужную.  \n" +
                                "Если среди команд нет нужной для вас команды напишите менеджеру /manager.");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // Обработка других команд и сообщений
                    handleMessage(chatId, messageText);
                }
            }
        }
    }




    private void handleMessage(long chatId, String messageText) {
        sendMessage(chatId, "Извините я вас не понял( \n " +
                "Посмотрите пожалуйста список команд и выберете нужную.  \n" +
                "Если среди команд нет нужной для вас команды напишите менеджеру /manager ");
    }

    // Ваш обработчик команды /start
    private void handleStartCommand(Message message) {
        long chatId = message.getChatId();
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.setText("Добро пожаловать! Это бот для заказа одежды из Китая через Poizon.");
        response.setReplyMarkup(createKeyboard());

        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Разрешить изменение размера кнопок под экран
        keyboardMarkup.setOneTimeKeyboard(true); // Скрыть клавиатуру после использования

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Калькулятор цены");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Курс Юаня");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Сделать заказ");

        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }
    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
    @Override
    public void onRegister() {
        super.onRegister();
    }
    private void registerUser(Message msg) {

        if (userRepository.findById(msg.getChatId()).isEmpty()) {

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegistredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("user saved: " + user);
        }
    }
}
