package com.github.jaraya81;

import com.github.jaraya81.bot.ManagerBot;
import com.github.jaraya81.bot.TriviaBot;
import com.github.jaraya81.enums.Property;
import com.github.jaraya81.exception.TelegramException;
import com.github.jaraya81.service.ServiceBot;
import com.github.jaraya81.util.Properties;
import com.pengrad.telegrambot.request.SetWebhook;
import lombok.extern.slf4j.Slf4j;

import static com.github.jaraya81.util.Condition.check;
import static spark.Spark.port;
import static spark.Spark.post;

@Slf4j
public class App {
    public static void main(String[] args) throws TelegramException {
        log.info("Init microservice...");

        String fileProperties = args.length == 1 ? args[0] : null;

        port(Integer.parseInt(Properties.get(Property.PORT.name(), fileProperties)));

        String appSite = Properties.get(Property.APP_SITE.name(), fileProperties);
        check(appSite != null && !appSite.isEmpty(), "appSite not found");

        log.info(Property.APP_SITE.name() + ": " + appSite);

        ServiceBot service = new ServiceBot();

        TriviaBot trivia = TriviaBot.getInstance()
                .name(Properties.get(Property.TRIVIA_NAME_BOT.name(), fileProperties))
                .token(Properties.get(Property.TRIVIA_TOKEN_BOT.name(), fileProperties))
                .addService(service)
                .initBot();
        post("/" + trivia.getToken(), trivia);
        trivia.getBot().execute(new SetWebhook().url(appSite + "/" + trivia.getToken()));

        ManagerBot manager = ManagerBot.getInstance()
                .name(Properties.get(Property.TRIVIA_NAME_BOT.name(), fileProperties))
                .token(Properties.get(Property.TRIVIA_TOKEN_BOT.name(), fileProperties))
                .addService(service)
                .initBot();
        post("/" + manager.getToken(), manager);
        manager.getBot().execute(new SetWebhook().url(appSite + "/" + manager.getToken()));
    }
}
