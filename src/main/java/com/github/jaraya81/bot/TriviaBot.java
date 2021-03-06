package com.github.jaraya81.bot;

import com.github.jaraya81.enums.Error;
import com.github.jaraya81.enums.Mensaje;
import com.github.jaraya81.enums.MsgType;
import com.github.jaraya81.model.Trivia;
import com.github.jaraya81.model.User;
import com.github.jaraya81.service.ServiceBot;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.github.jaraya81.util.Condition.check;

@Slf4j
public class TriviaBot implements Route {

    private static final String START = "/start";
    private static final String OK = "OK";
    private static final String NOK = "NOK";
    private static final String PREFIX = "887";
    private String token;
    private String name;
    private TelegramBot bot;
    private ServiceBot serviceBot;
    private final Mensaje mensaje = new Mensaje();

    private TriviaBot() {
        super();
    }

    public static TriviaBot getInstance() {
        return new TriviaBot();
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object handle(Request request, Response response) {
        Update update = BotUtils.parseUpdate(request.body());
        if (update == null) {
            return "UPDATE NOK";
        }

        MsgType type = identifyMsg(update);
        if (type == MsgType.START) execUser(update);
        else if (type == MsgType.SN) execSN(update);
        else if (type == MsgType.ENTER_TRIVIA) execTrivia(update);
        return "OK";
    }

    private void execTrivia(Update update) {
        User user = serviceBot.getUser(update.message().from().id());

        SendResponse sendResponse;
        if (user.getUsernameSN() == null || user.getUsernameSN().isEmpty()) {
            sendResponse = bot.execute(
                    new SendMessage(update.message().chat().id(), mensaje.enterTriviaFail(update.message().from().languageCode()))
                            .parseMode(ParseMode.HTML)
                            .disableWebPagePreview(false)
                            .disableNotification(false));
            log.info(update.message().chat().id() + " :: " + (sendResponse.isOk() ? OK : NOK));
        } else {
            Trivia trivia = serviceBot.getTrivia(update.message().text());
            if (trivia != null) {
                sendResponse = bot.execute(
                        new SendMessage(update.message().chat().id(), mensaje.enterTrivia(update.message().from().languageCode()))
                                .parseMode(ParseMode.HTML)
                                .disableWebPagePreview(false)
                                .disableNotification(false));
            } else {
                sendResponse = bot.execute(
                        new SendMessage(update.message().chat().id(), mensaje.triviaNotExist(update.message().from().languageCode()))
                                .parseMode(ParseMode.HTML)
                                .disableWebPagePreview(false)
                                .disableNotification(false));
            }
        }
        log.info(update.message().chat().id() + " :: " + (sendResponse.isOk() ? OK : NOK));

    }

    private void execSN(Update update) {
        User user = serviceBot.getUser(update.message().from().id());
        if (user != null) {
            user.setUsername(update.message().from().username());
            user.setUsernameSN(update.message().text().replace(mensaje.sn(update.message().from().languageCode()), "").trim());
            serviceBot.updateUser(user);
            SendResponse sendResponse = bot.execute(
                    new SendMessage(update.message().chat().id(), mensaje.snUpdate(update.message().from().languageCode()))
                            .parseMode(ParseMode.HTML)
                            .disableWebPagePreview(false)
                            .disableNotification(false));
            log.info(update.message().chat().id() + " :: " + (sendResponse.isOk() ? OK : NOK));
        }

    }

    private void execUser(Update update) {
        User user = serviceBot.getUser(update.message().from().id());
        if (user == null) {
            check(serviceBot.saveUser(User.builder().build()) != null, "Error creando usuario");
            SendResponse sendResponse = bot.execute(
                    new SendMessage(update.message().chat().id(), mensaje.start(update.message().from().languageCode()))
                            .parseMode(ParseMode.HTML)
                            .disableWebPagePreview(false)
                            .disableNotification(false));
            log.info(update.message().chat().id() + " :: " + (sendResponse.isOk() ? OK : NOK));
        } else {
            user.setUsername(update.message().from().username());
            serviceBot.updateUser(user);
            SendResponse sendResponse = bot.execute(
                    new SendMessage(update.message().chat().id(), mensaje.back(update.message().from().languageCode()))
                            .parseMode(ParseMode.HTML)
                            .disableWebPagePreview(false)
                            .disableNotification(false));
            log.info(update.message().chat().id() + " :: " + (sendResponse.isOk() ? OK : NOK));
        }
    }

    private MsgType identifyMsg(Update update) {
        if (update.message() == null) return MsgType.NOT_FOUND;
        if (update.message().text() == null) return MsgType.NOT_FOUND;
        if (update.message().text().contentEquals(START)) return MsgType.START;
        if (update.message().text().startsWith(mensaje.sn(update.message().from().languageCode()))) return MsgType.SN;
        if (update.message().text().startsWith(PREFIX)) return MsgType.ENTER_TRIVIA;
        //TODO HACER
        return MsgType.NOT_FOUND;
    }

    public TriviaBot token(String token) {
        this.token = token;
        return this;
    }

    public TriviaBot name(String name) {
        this.name = name;
        return this;
    }

    public TelegramBot getBot() {
        return bot;
    }

    public TriviaBot addService(ServiceBot service) {
        this.serviceBot = service;
        return this;
    }

    public TriviaBot initBot() {
        check(token != null, token + " :: " + Error.NULL.name());
        bot = new TelegramBot.Builder(token).build();
        return this;
    }
}
