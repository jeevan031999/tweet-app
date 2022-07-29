package com.cts.tweetapp.constants;

public class Constants {
    private Constants(){}

    public static final String BASE_URL="/api/v1.0/tweets";
    public static final String REGISTER="/register";
    public static final String LOGIN="/login";
    public static final String ALL_USER="/allUser";
    public static final String BY_ID="/getById";

    public static final String COMMON="/getSimilarUsername";

    //---------------------------------Tweet Constant--------------------------------
    public static final String ADD_TWEET="/{username}/add";

    public static final String EDIT_TWEET="/{username}/update/{tweetId}";

    public static final String ALL_TWEET="/username";

    public static final String TWEET_BY_ID="/";

    public static final String DELETE_TWEET="/{username}/delete/{id}";

    public static final String LIKE_TWEET="/{username}/like/{id}";

    public static final String COMMENTS="/<username>/reply/<id>";



}
