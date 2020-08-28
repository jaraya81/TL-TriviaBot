package com.github.jaraya81.bot;

import com.github.jaraya81.enums.Error;
import com.github.jaraya81.exception.TelegramException;
import com.github.jaraya81.service.ServiceBot;
import com.pengrad.telegrambot.TelegramBot;
import spark.Request;
import spark.Response;
import spark.Route;

import static com.github.jaraya81.util.Condition.check;

public class ManagerBot implements Route {

    private String token;
    private String name;
    private TelegramBot bot;
    private ServiceBot serviceBot;

    private ManagerBot() {
        super();
    }

    public static ManagerBot getInstance() {
        return new ManagerBot();
    }


    public String getToken() {
        return token;
    }

    @Override
    public Object handle(Request request, Response response) throws TelegramException {
        return null;
    }

    public ManagerBot token(String token) {
        this.token = token;
        return this;
    }

    public ManagerBot name(String name) {
        this.name = name;
        return this;
    }

    public TelegramBot getBot() {
        return bot;
    }

    public ManagerBot addService(ServiceBot service) {
        this.serviceBot = service;
        return this;
    }

    public ManagerBot initBot() {
        check(token != null, token + " :: " + Error.NULL.name());
        bot = new TelegramBot.Builder(token).build();
        return this;
    }

}
