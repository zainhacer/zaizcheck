package tn.kids.game.utils;
import java.io.Serializable;


public class Constant  implements Serializable{

    //please change publisher id in androidmanifest

    public static final boolean show_admob_banner=true ; // show AdMob Smart banner
    public static final boolean show_admob_interstitial = true; // show AdMob Interstitial
    public static final String adMob_key_banner = "ca-app-pub-3940256099942544/6300978111";
    public static final String adMob_key_interstitial = "ca-app-pub-3940256099942544/1033173712";

    public static Boolean show_banner_ad = true; // show reward video
    public static String rewarded_ad_id="ca-app-pub-3940256099942544/5224354917";
    public static final int reward_time_inseconds=60;//set seconds to win after watching the reward video
    public static final int number_of_hearts=4;


    public static final int time_inseconds=180;



    //array contains the names of images in drawable folder
    public static final String[] imageQuestions = {"backpack","book","calculator","chalkboard","eraser","flashdisk","globe","handlens","labflask","notebooks","palette","pencils","setsquare","student"};
    //array contains the answers of quetions  in my case ive used the same names for questions and answers
    public static final String[] questionsAnswers = {"backpack","book","calculator","chalkboard","eraser","flashdisk","globe","handlens","labflask","notebooks","palette","pencils","setsquare","student"};

}
