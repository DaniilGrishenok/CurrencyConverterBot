package com.rhythmpoizon.currencyconverterbot.service;

import com.rhythmpoizon.currencyconverterbot.botapi.handlers.InfoCommandHandler;
import com.rhythmpoizon.currencyconverterbot.botapi.handlers.ManagerCommandHandler;
import com.rhythmpoizon.currencyconverterbot.botapi.handlers.PriceCommandHandler;
import com.rhythmpoizon.currencyconverterbot.botapi.handlers.CurrencyCommandHandler;
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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import static com.rhythmpoizon.currencyconverterbot.botapi.handlers.PriceCommandHandler.TEXT_PRICE;


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
    CurrencyCommandHandler currencyCommandHendler;

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
    String photoFilePath = "src/main/resources/static/images/photo_2023-10-07_22-11-01.jpg";
    // Указываем подпись к фото
    static final String WELCOME_TEXT = "Добро пожаловать в мир POIZON! 🌟\n\n" +
            "Если вы не знаете, как оформить заказ, не волнуйтесь, мы готовы помочь вам в этом! 🛒💼\n\n" +
            "Не стесняйтесь обратиться к нам за помощью! Мы - ваш надежный партнер в мире моды и шопинга на популярной платформе POIZON. 🤝✨\n\n" +
            "POIZON - это онлайн-платформа для покупки одежды, обуви и аксессуаров в Китае. Мы помогаем вам сделать заказы и организовываем доставку из Китая в Россию. Ваш комфорт и удовлетворение наш главный приоритет. Доверьтесь нам и наслаждайтесь стильными покупками!\n\n" +
            "Если у вас возникли вопросы или нужна помощь, не стесняйтесь связаться с нами. Мы всегда готовы помочь сделать ваш опыт с POIZON приятным и удобным. 💼🌍";
    private Map<Long, BotState> chatStates = new HashMap<>();

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start") ||
                messageText.equals("hello") ||
                messageText.equals("привет")) {
                handleStartCommand(update.getMessage());
                //sendPhotoToChat(chatId,WELCOME_TEXT,photoFilePath);

            } else if (messageText.equals("/currency") ||
                       messageText.equals("\uD83C\uDDE8\uD83C\uDDF3 \u00A5 Курс Юаня")||
                       messageText.equals("курс")) {
                currencyCommandHendler.handleCurrencyCommand(chatId);
                chatStates.remove(chatId);

            } else if (messageText.equals("/info") ||
                      (messageText.equals("❓ FAQ"))) {
                infoCommandHandler.handleInfoCommand(chatId);
                chatStates.remove(chatId);

            } else if (messageText.equals("/manager")||
                       messageText.equals("заказ") ||
                       messageText.equals("\uD83D\uDECD Сделать заказ") ) {
                managerCommandHandler.handleManagerCommand(chatId);
                chatStates.remove(chatId);

            } else if (messageText.equals("/delivery")||
                       messageText.equals("\uD83D\uDE9A Доставка")) {
                infoCommandHandler.handleDeliveryCommand(chatId);
                chatStates.remove(chatId);

            }
            else if (messageText.equals("/dima")) {
             sendMessage(965052217, "Привет Дима");

            }
           else if (messageText.equals("/price")||
                    messageText.equals("\uD83D\uDCB8 Калькулятор цен")) {
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
                    sendPhotoToChat(chatId,TEXT_PRICE,photoFilePath);
                }
            } else {
                if (chatStates.containsKey(chatId) && chatStates.get(chatId) == BotState.WAITING_FOR_PRICE) {
                    // Если бот находится в режиме ожидания цены, обрабатываем ввод пользователя как цену
                    try {
                        double priceInYuan = Double.parseDouble(messageText);
                        priceCommandHandler.handlePriceCommand(chatId, priceInYuan);
                        chatStates.remove(chatId);
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Пожалуйста введите корректную сумму в Юанях.");
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


    public void sendPhotoToChat(long chatId, String photoCaption, String photoFilePath) {
        // Создаем объект SendPhoto
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId)); // Устанавливаем идентификатор чата
        sendPhoto.setCaption(photoCaption); // Устанавливаем подпись к фото

        // Создаем объект InputFile для загрузки фотографии
        InputFile photoInputFile = new InputFile(new File(photoFilePath));

        // Устанавливаем фотографию
        sendPhoto.setPhoto(photoInputFile);

        try {
            // Отправляем фото
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    // Ваш обработчик команды /start
    private void handleStartCommand(Message message) {

        String START_TEXT = "";
        long chatId;
        chatId = message.getChatId();
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.enableHtml(true);

        response.setText("\uD83C\uDFE0 Главное меню");
        response.setReplyMarkup(createKeyboard());
        registerUser(message);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Разрешить изменение размера кнопок под экран
        keyboardMarkup.setOneTimeKeyboard(false); // Скрыть клавиатуру после использования

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("\uD83D\uDCB8 Калькулятор цен");
        row1.add("\uD83C\uDDE8\uD83C\uDDF3 \u00A5 Курс Юаня");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("\uD83D\uDECD Сделать заказ");
        row2.add("\uD83D\uDE9A Доставка");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("❓ FAQ");

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
        message.enableHtml(true);
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
