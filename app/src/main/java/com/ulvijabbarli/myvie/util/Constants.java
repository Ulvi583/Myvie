package com.ulvijabbarli.myvie.util;

public class Constants {


    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String PREF_APP_ACCESS_TOKEN = "PREF_APP_ACCESS_TOKEN";
    public static final String API_KEY_FOR_MOVIES = "ee6a87b38983d53056c0a7fc01f0c7c0";

    public interface RequestSatus {
        int CODE_SUCCESS = 1;
        int CODE_INVALID_TOKEN = 2;
        int CODE_DATA_NOT_FOUND_SEARCHED = 4;
    }

    public interface Language {
        String DEFAULT_LANG = "az";
        String LANG_NOT_SET = "LANG_NOT_SET";
        String LANG_EN = "en";
        String LANG_AZ = "az";
        String LANG_RU = "ru";
        String PREF_LANG = "PREF_LANG";
    }

    public interface Common {
        int SPLASH_DISPLAY_LENGTH = 1000;
        int REQUEST_CODE = 1000;
        String VERIFICATION_ID = "VERIFICATION_ID";
        int COUNTRY_ID_AZ = 1;
        String SORT_BY_RATE = "SORT_BY_RATE";
        String SORT_BY_DISTANCE = "SORT_BY_DISTANCE";
        String CURRENT_LATITUDE = "CURRENT_LATITUDE";
        String CURRENT_LONGITUDE = "CURRENT_LONGITUDE";
        String IS_24_HOUR = "IS_24_HOUR";
        String IS_NOT_24_HOUR = "IS_NOT_24_HOUR";
    }

    public interface RequestParams {
        String PHONE = "phone";
        String COUNTRY_ID = "country_id";
        String U_ID = "uid";
    }
}
