package com.github.jaraya81.enums;

import java.util.HashMap;
import java.util.Map;

public class Mensaje {


    private static final String START = "START";

    private static final String ES = "es";
    private static final String EN = "en";

    private static final String BACK = "BACK";
    private static final String SN = "SN";
    private static final String SN_UPDATE = "SN_UPDATE";
    private static final String ENTER_TRIVIA_FAIL = "ENTER_TRIVIA_FAIL";
    private static final String ENTER_TRIVIA = "ENTER_TRIVIA";
    private static final String TRIVIA_NOT_EXIST = "TRIVIA_NOT_EXIST";

    private Map<String, String> es = new HashMap<>();
    private Map<String, String> en = new HashMap<>();

    public Mensaje() {
        super();
        //TODO INSTANCIA MAPAS
    }

    public String start(String lang) {
        return get(lang, START);

    }

    public String back(String lang) {
        return get(lang, BACK);
    }

    public String sn(String lang) {
        return get(lang, SN);
    }

    public String snUpdate(String lang) {
        return get(lang, SN_UPDATE);
    }

    private String get(String lang, String code) {
        if (lang == null || lang.isEmpty() || lang.contentEquals(lang)) return es.get(code);
        if (lang.contentEquals(EN)) return en.get(code);
        return es.get(code);
    }

    public String enterTriviaFail(String languageCode) {
        return get(languageCode, ENTER_TRIVIA_FAIL);
    }

    public String enterTrivia(String languageCode) {
        return get(languageCode, ENTER_TRIVIA);
    }

    public String triviaNotExist(String languageCode) {
        return get(languageCode, TRIVIA_NOT_EXIST);
    }
}
