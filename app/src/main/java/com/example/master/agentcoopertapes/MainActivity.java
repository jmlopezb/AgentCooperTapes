package com.example.master.agentcoopertapes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp3;
    int position=0;
    private List<Tapes> cooperTapes;
    private ParseJson parse;
    private TextView Tv;
    private TextView TvTitle;
    private Handler mHandler = new Handler();

    private int trackSeek=0;
    boolean idioma=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp3 = MediaPlayer.create(this, R.raw.tapescoop);
        InputStream is;
        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
         idioma=prefe.getBoolean("idioma",true);


        if(idioma){
        is= getResources().openRawResource(R.raw.spanish);}
        else{
        is= getResources().openRawResource(R.raw.english);}

        try
        {
         parse=new ParseJson();
         cooperTapes=parse.readJsonStream(is);

        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        trackSeek=prefe.getInt("track",0);

        Tv=(TextView)findViewById(R.id.textView);
        Tv.setMovementMethod(new ScrollingMovementMethod());
        Tv.setText(cooperTapes.get(trackSeek).getTranscription());

        TvTitle=(TextView)findViewById(R.id.textViewChapter);
        TvTitle.setText(cooperTapes.get(trackSeek).getChapterName());

        mp3.seekTo(cooperTapes.get(trackSeek).getTimerStar());

        updatePosition updatePosition1 = new updatePosition();
        mHandler.postDelayed(updatePosition1, 500);



    }

    public void playSong(View View) {


        if (mp3.isPlaying()) {
            position = mp3.getCurrentPosition();
            mp3.pause();
        }
        else
        {
            mp3.start();
        }

        if(position>=2640000)
        {
            mp3.seekTo(0);
            Tv.setText(cooperTapes.get(0).getTranscription());
            TvTitle.setText(cooperTapes.get(trackSeek).getChapterName());
            trackSeek=0;
        }

    }

    public void backsong(View view)
    {
        if(trackSeek!=0) {

            trackSeek--;
            mp3.seekTo(cooperTapes.get(trackSeek).getTimerStar());
            Tv.setText(cooperTapes.get(trackSeek).getTranscription());
            TvTitle.setText(cooperTapes.get(trackSeek).getChapterName());
            Tv.scrollTo(0,0);
        }
    }


    public void forward(View view) {

            if(trackSeek<40) {

                trackSeek++;
                mp3.seekTo(cooperTapes.get(trackSeek).getTimerStar());
                Tv.setText(cooperTapes.get(trackSeek).getTranscription());
                TvTitle.setText(cooperTapes.get(trackSeek).getChapterName());
                Tv.scrollTo(0,0);

            }
    }



    class updatePosition implements Runnable{
        public void run() {
            {
                if (mp3!=null ) {
                    position = mp3.getCurrentPosition();
                    if (position > cooperTapes.get(trackSeek).getTimerend()) {
                        trackSeek++;
                        updateSong(trackSeek);

                    }
                    if(position>=2640000)
                    {
                        mp3.pause();
                    }

                    mHandler.postDelayed(this, 500);
                }


            }
        }

    }

        public void updateSong(int trackSeek)
        {
            Tv.setText(cooperTapes.get(trackSeek).getTranscription());
            TvTitle.setText(cooperTapes.get(trackSeek).getChapterName());
            Tv.scrollTo(0,0);
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        SubMenu sm= menu.addSubMenu(0,44,0,"idioma");
        sm.add(0,41,0,"inglés");
        sm.add(0,42,0,"español");

        SubMenu sm2= menu.addSubMenu(0,44,0,"capítulos");
        menu.add(0,43,0,"about...");



        for(int i=0;i<41;i++) {

              sm2.add(1, i, 0, cooperTapes.get(i).getChapterName().toString());
        }


         return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id==41)
        {
            SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferencias.edit();
            editor.putBoolean("idioma", false);
            editor.commit();


            mp3.release();
            mp3=null;
            Intent intent = getIntent();
            finish();

            startActivity(intent);


        }
       else if (id==42)
        {
            SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferencias.edit();
            editor.putBoolean("idioma", true);
            editor.commit();


            mp3.release();
            mp3=null;
            Intent intent = getIntent();
            finish();

            startActivity(intent);


        }

        else if(id==43)
        {
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage("Autor: Martin Lopez Blanco \n Email: josemartin.lopez.blanco@gmail.com \n").setTitle("about...");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else if(id==44){}

        else{

            trackSeek=id;
            mp3.seekTo(cooperTapes.get(trackSeek).getTimerStar());
            Tv.setText(cooperTapes.get(trackSeek).getTranscription());
            TvTitle.setText(cooperTapes.get(trackSeek).getChapterName());
            Tv.scrollTo(0,0);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override

   protected void onPause()
    {
        super.onPause();

        SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        if(trackSeek==40)
        {
            editor.putInt("track", 0);
        }
        else{editor.putInt("track", trackSeek);}

        editor.commit();

    }

}






