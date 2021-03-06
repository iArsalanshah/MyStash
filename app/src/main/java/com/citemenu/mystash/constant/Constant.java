package com.citemenu.mystash.constant;

public class Constant {
    public static final String BASE_URL = "http://www.mystash.ca";//http://www.mystash.ca/mobileservice_new.php

    public static final String ACTION_REGISTER_CUSTOMER = "customer_register";
    public static final String ACTION_UPDATE_REGISTER_CUSTOMER = "customer_register_edit";
    public static final String ACTION_LOGIN_CUSTOMER = "customer_login";
    public static final String ACTION_GET_MYSTASH_LIST = "get_my_stash_list";
    public static final String ACTION_GET_RESTAURANT_LIST_FOR_CHECKIN = "get_restaurant_list_for_checkin";
    public static final String ACTION_GET_RESTAURANT_LIST_FOR_CHECKIN_MAP = "get_restaurant_list_for_checkin_map";
    public static final String ACTION_FB_LOGIN = "customer_fblogin";
    public static final String ACTION_FORGOT_PWD = "forgot_password";
    public static final String ACTION_ADD_STASH = "add_my_stash";
    public static final String ACTION_REMOVE_STASH = "delete_my_stash";
    public static final String ACTION_GET_STAMPS = "get_customer_stamps";
    public static final String ACTION_ADD_RATING = "add_rating";
    public static final String ACTION_ADD_REVIEW = "add_reviews";
    public static final String ACTION_GET_MY_LOYALTY_CARDS = "customer_my_loyalty_card";
    public static final String ACTION_GET_LOYALTY_CARDS_LIST = "customer_get_loyalty_card_list";
    public static final String ACTION_ADD_LOYALTY_CARD = "customer_add_loyalty_card";
    public static final String ACTION_EDIT_LOYALTY_CARD = "edit_customer_loyalty_card";
    public static final String ACTION_GET_CATEGORIES_COUPONS = "customer_get_coupon_categories";
    public static final String ACTION_GET_MY_SAVED_COUPONS = "customer_get_saved_coupons";
    public static final String ACTION_GET_ALL_COUPONS_LIST = "customer_get_coupons";
    public static final String ACTION_SAVE_A_COUPON = "customer_save_coupons";
    public static final String ACTION_GET_COUPONS_BY_ADMIN = "get_coupons_by_adminid";
    public static final String PREFS_NAME = "LoginPrefs";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String LOG_TAG = "MyStash_LOG_TAG";
    public static final String USER_NAME = "username";
    public static final String USER_ID = "userId";
    public static final String USER_LAT = "userLat";
    public static final String USER_LONG = "userLong";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_GENDER = "userGender";
    public static final String USER_OBJECT = "userObject";
    public static final String ACTION_REMINDME_COUPON = "remindme_coupon";
    public static final String ACTION_REDEEM_COUPON = "customer_redeem_coupons";
    public static final String ACTION_UPLOAD_LOYALTY_IMAGE = "upload_loyalty_image";
    public static final String ACTION_UPLOAD_PROFILE_IMAGE = "upload_image";
    public static final String ACTION_CUSTOMER_CHECKIN = "customer_checkin";
    public static final float DEFAULT_RADIUS = 5000;

    public static final String ACTION_DELETE_LOYALTY_CARD = "delete_customer_loyalty_card";
    public static final String ACTION_GET_CITE_POINTS = "get_customer_transactions";
    public static final String ACTION_GET_ALL_FLYERS = "get_all_flyers";

    public static final String ACTION_CAMERA = "action-camera";
    public static final String ACTION_GALLERY = "action-gallery";
    public static final String IMAGE_PATH = "image-path";
    public static final String EXTRA_IMAGE_PATH = "CameraActivity.EXTRA_IMAGE_PATH";
    public static final String ACTION_GET_PROFILE_FLYERS = "get_profile_flyers";
    public static final String ACTION_DELETE_NOTIFICATION = "delete_notification";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer"; //asdsad

    public static final String REGISTRATION_COMPLETE = "registrationComplete"; //asdas
    public static final String ACTION_GET_MESSAGES = "get_customer_notifications";
    public static final String ACTION_GET_STAMPS_COUNT = "get_customer_stampcount";

    public static final String SHARE_SUBJECT = "MyStash App";

    //@"Check out MyStash App. The best loyalty app on the market.\nStore all your loyalty cards, find many more loyalty programs
    // to all your favorite businesses, savings, special VIP offers,
    /// and MUCH MUCH more. Download it today.\n\nhttp://www.mystashapp.ca/";
    public static final String WEB_URL = "http://www.mystashapp.ca/";
    public static final String SHARE_PROGRAM_STAMP_TEXT_START = "Check out this great company i found on MyStash.\n";
    public static final String SHARE_APP_TEXT = "Check out MyStash App. The best loyalty app on the market.\n" +
            "Store all your loyalty cards, find many more loyalty programs to all your favorite businesses, savings, " +
            "special VIP offers, and MUCH MUCH more. Download it today.\n\n";

    public static final String SHARE_LINK = "http://mystash.ca/openurl.php";
    public static final String RESPONSE_NULL = "Found null in web response";
    public static final String RESPONSE_ON_FAILURE = "Something went wrong. Please try again";

    public static final long BACK_BTN_TIME_INTERVAL = 2 * 1000;


}