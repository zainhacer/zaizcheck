package tn.kids.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tn.kids.game.utils.Constant;
import tn.kids.game.utils.CustomDialogClass;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import android.view.View.OnTouchListener;
import android.view.View.OnDragListener;

public class QuestionActivity extends Activity implements OnTouchListener, OnDragListener,RewardedVideoAdListener{
    String[] quesList;
    static int  score,hearts;
    int qid = 0;
    String[]perfect_position_array;
    String currentQ;
    String currentA;
    TextView  times, scored;
    ImageView imgQuestion;
    private RewardedVideoAd mAd;
    public static Context contextstat;
   public static ProgressDialog pDialog ;
    int width;
    private float originalX = 0;
    private float originalY = 0;
     LinearLayout answers_linearLayout22;
    LinearLayout placeholder_linearLayout;
    DisplayMetrics displayMetrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

         displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);



        quesList= Constant.imageQuestions;
        currentQ = Constant.imageQuestions[qid];
        currentA = Constant.questionsAnswers[qid];

        imgQuestion = (ImageView) findViewById(R.id.imgQuestion);






        scored = (TextView) findViewById(R.id.score);
        score = 0;
        hearts = Constant.number_of_hearts;
        setHearts(hearts);

        times = (TextView) findViewById(R.id.timers);


        setQuestionView();
        times.setText("00:00:60");


        CounterClass timer = new CounterClass(Constant.time_inseconds*1000, 1000);
        timer.start();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putBoolean("firstrun", true)
                .apply();

        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putBoolean("firstrunhearts", true)
                .apply();




        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        contextstat=QuestionActivity.this;
        pDialog= new ProgressDialog(QuestionActivity.this);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

    }//oncreate



    public static int getMyValue(){return score;}

    public  void loadRewardedVideo() {
            mAd.loadAd(Constant.rewarded_ad_id,
                    new AdRequest.Builder()
                            .build());
    }



    public void getAnswer(String AnswerString) {

        if (AnswerString.equals("win")) {
            answers_linearLayout22.removeAllViews();
            placeholder_linearLayout.removeAllViews();


            score++;
            scored.setText(getString(R.string.score) +" "+ score);
        } else {

          /*  Intent intent = new Intent(QuestionActivity.this,ResultActivity.class);
            Bundle b = new Bundle();
            b.putInt("score", score);
            intent.putExtras(b);
            startActivity(intent);
            finish();*/
        }
        if (qid < Constant.imageQuestions.length) {


            currentQ = Constant.imageQuestions[qid];
            currentA = Constant.questionsAnswers[qid];
            setQuestionView();
        } else {


            Intent intent = new Intent(QuestionActivity.this,won.class);
            Bundle b = new Bundle();
            b.putInt("score",score);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }


    }




    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }


        @SuppressLint("SuspiciousIndentation")
        @Override
        public void onFinish() {

            if (Constant.show_banner_ad
                    && ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
            if (firstrun){
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit()
                        .putBoolean("firstrun", false)
                        .apply();
                pDialog.setMessage(getString(R.string.loading));


                CustomDialogClass cdd=new CustomDialogClass(QuestionActivity.this);
                cdd.setCanceledOnTouchOutside(false);
                cdd.setCancelable(false);
                if(!((Activity) QuestionActivity.this).isFinishing())
                cdd.show();
            }
            else{
                Intent intent = new Intent(QuestionActivity.this,ResultActivity.class);
                Bundle b = new Bundle();
                b.putInt("score", score);
                intent.putExtras(b);
                startActivity(intent);
                finish();

            }
            }
            else{
                Intent intent = new Intent(QuestionActivity.this,ResultActivity.class);
                Bundle b = new Bundle();
                b.putInt("score", score);
                intent.putExtras(b);
                startActivity(intent);
                finish();

            }
        }//onfinish

      @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(millis)));
            System.out.println(hms);
            times.setText(hms);






        }


    }



    public void setHearts(int nhearts){
        final LinearLayout linear_hearts = (LinearLayout) findViewById(R.id.linear_hearts);
        linear_hearts.removeAllViews();
    int total_hearts=Constant.number_of_hearts;
    for (int i=1;i<=total_hearts;i++){
        ImageView iv = new ImageView(getApplicationContext());

        if (i<=nhearts){
            iv.setImageDrawable(getDrawable(R.drawable.hearts));
        }
        else{
            iv.setImageDrawable(getDrawable(R.drawable.hearts_empty));
        }
        LayoutParams lp= new LayoutParams(70,70);
        iv.setLayoutParams(lp);
        linear_hearts.addView(iv);
    }


    }

    private void setQuestionView() {

        perfect_position_array = new String[currentA.length()];



        if (currentA.length()<7)
        {
            width = displayMetrics.widthPixels/7;
        }
        else{
            width = displayMetrics.widthPixels/currentA.length();
        }



        imgQuestion.setImageDrawable(getDrawable(this.getResources().getIdentifier(currentQ, "drawable", this.getPackageName())));


        final LinearLayout answers_linearLayout = (LinearLayout) findViewById(R.id.answers_linearLayout);
       answers_linearLayout22 = (LinearLayout) findViewById(R.id.answers_linearLayout22);
        answers_linearLayout.setOnDragListener(this);

        ImageView[] imageArray = new ImageView[currentA.length()];


        for(int i=0; i<currentA.length(); i++) {
            ImageView iv = new ImageView(getApplicationContext());
            iv.setId(i);
            iv.setImageDrawable(getDrawable(this.getResources().getIdentifier(currentA.charAt(i)+"", "drawable", this.getPackageName())));
            LayoutParams lp= new LayoutParams(width,width);



            iv.setLayoutParams(lp);
            iv.setOnTouchListener(this);
            iv.setOnDragListener(this);
            imageArray[i] = iv;
           // perfect_position_array[i]=currentA.charAt(i)+","+iv.getId()*width;
            int iddd=9999;
            int idd=iv.getId()+iddd;
            perfect_position_array[i]=currentA.charAt(i)+","+idd;


        }

        shuffleArray(imageArray);
        for(int i=0; i<imageArray.length; i++) {
            answers_linearLayout22.addView(imageArray[i]);
        }
        placeholder_linearLayout = (LinearLayout) findViewById(R.id.placeholder_linearLayout);
        for(int i=0; i<currentA.length(); i++) {
            LinearLayout ln = new LinearLayout(getApplicationContext());
            ln.setBackground(getDrawable(R.drawable.placeholder));
            ln.setId(9999+i);
            LayoutParams lp = new LayoutParams(width,width);
            ln.setLayoutParams(lp);
            ln.setOnDragListener(this);
            placeholder_linearLayout.addView(ln);
        }
            qid++;
    }



    static void shuffleArray(ImageView[] arr)
    {
        Random rnd = new Random();
        for (int i = arr.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Swap
            ImageView a = arr[index];
            arr[index] = arr[i];
            arr[i] = a;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            originalX = motionEvent.getX();
            originalY = motionEvent.getY();
            int id= view.getId();
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(null, shadowBuilder, view, 0);
           // view.setVisibility(View.INVISIBLE);
            return true;
        }


        else {
            return false;
        }
    }
    @SuppressLint("ResourceType")
    @Override
    public boolean onDrag(View layoutview, DragEvent dragevent) {

        View vvv = (View) dragevent.getLocalState();
        int img_id=vvv.getId();
        int perfect_position=img_id*width;

        int action = dragevent.getAction();
        View view = (View) dragevent.getLocalState();

       /* if ((layoutview.getId()>=9999 && layoutview.getId()<=(9999+currentA.length()))
        )
        {layoutview.setBackground(getDrawable(R.drawable.placeholder));}*/

        switch (action) {

            case DragEvent.ACTION_DRAG_STARTED:
                view.setVisibility(View.INVISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d("perfect_position_array", "arr: " + Arrays.toString(perfect_position_array));
                Log.d("perfect_position_array", "arr: " + layoutview.getId()+"");



                if (Arrays.asList(perfect_position_array).contains(currentA.charAt(img_id)+","+(int)layoutview.getId())
                        &&(layoutview.getId()>=9999 && layoutview.getId()<=(9999+currentA.length()))
                )
                {layoutview.setBackground(getDrawable(R.drawable.placeholder_true));}
                else if (!Arrays.asList(perfect_position_array).contains(currentA.charAt(img_id)+","+(int)layoutview.getId())
                        &&(layoutview.getId()>=9999 && layoutview.getId()<=(9999+currentA.length()))
                )
                {layoutview.setBackground(getDrawable(R.drawable.placeholder_wrong));}

                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if ((layoutview.getId()>=9999 && layoutview.getId()<=(9999+currentA.length()))
                )
                {layoutview.setBackground(getDrawable(R.drawable.placeholder));}
                break;
            case DragEvent.ACTION_DROP:
                if ((layoutview.getId()>=9999 && layoutview.getId()<=(9999+currentA.length()))
                )
                {layoutview.setBackground(getDrawable(R.drawable.placeholder));}

                int childCount = ((ViewGroup)answers_linearLayout22).getChildCount();
                ViewGroup owner = (ViewGroup) view.getParent();
                if (Arrays.asList(perfect_position_array).contains(currentA.charAt(img_id)+","+(int)layoutview.getId())
                        &&(layoutview.getId()>=9999 && layoutview.getId()<=(9999+currentA.length()))
                )
                {
                    List<String> list = new ArrayList<String>(Arrays.asList(perfect_position_array));
                    list.remove(currentA.charAt(img_id)+","+(int)layoutview.getId());
                    perfect_position_array = list.toArray(new String[0]);

                owner.removeView(view);
                LinearLayout container = (LinearLayout) layoutview;
                container.addView(view);
                if(container.getId()>=9999){
                    view.setOnTouchListener(null);
                    owner.setOnDragListener(null);
                }

                    MediaPlayer player = MediaPlayer.create(this, R.raw.correct);
                    player.setVolume(100,100);
                    player.start();

                }
                else{
                    hearts--;
                    if (hearts>0){
                        MediaPlayer player = MediaPlayer.create(this, R.raw.wrong);
                        player.setVolume(100,100);
                        player.start();
                        setHearts(hearts);

                    }
                    else{

                        Intent intent = new Intent(QuestionActivity.this,ResultActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("score", score);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    }//else hearts =0

                }

                if (childCount==1){
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.win));
                    LayoutParams lp = new LayoutParams(width,width);
                    iv.setLayoutParams(lp);
                    iv.setOnTouchListener(this);
                    iv.setOnDragListener(this);
                    answers_linearLayout22.addView(iv);
                    getAnswer("win");
                }


                break;
            case DragEvent.ACTION_DRAG_ENDED:
               // view.setVisibility(View.VISIBLE);




                view.post(new Runnable(){
                    @Override
                    public void run() {
                        view.setVisibility(View.VISIBLE);
                    }
                });



                break;
            default:
                break;
        }
        return true;
    }














    @Override
    public void onRewardedVideoAdLoaded() {
        try {
            if (mAd.isLoaded()) {
                pDialog.hide();
                mAd.show();
            }
            pDialog.hide();

        } catch (NullPointerException e) {
            e.printStackTrace();
            pDialog.hide();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoCompleted() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    public void onRewardedVideoAdClosed() {
        hearts=Constant.number_of_hearts;
        setHearts(Constant.number_of_hearts);
        CounterClass timer = new CounterClass(Constant.reward_time_inseconds*1000, 1000);
        timer.start();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
    }


}

