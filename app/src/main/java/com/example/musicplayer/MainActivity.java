package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button PlayButton;
    private SeekBar positionBar,volumeBar;
    private TextView elapsedTimeLable, remainingTimeLable;
    private MediaPlayer mediaPlayer;
    private int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        PlayButton = (Button) findViewById(R.id.playButton);
        elapsedTimeLable = (TextView) findViewById(R.id.elapsedTimeLable);
        remainingTimeLable = (TextView) findViewById(R.id.remainingTimeLable);

        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.files );
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(0.5f,0.5f);
        totalTime= mediaPlayer.getDuration();

        positionBar= (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    mediaPlayer.seekTo(progress);
                    positionBar.setProgress(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNumber =  progress/100f;
                mediaPlayer.setVolume(volumeNumber,volumeNumber);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
         new Thread(new Runnable() {
             @Override
             public void run() {
                 while (mediaPlayer != null)
                 {
                     try
                     {
                         Message message=new Message();
                         message.what = mediaPlayer.getCurrentPosition();
                         handler.sendMessage(message);
                         Thread.sleep(1000);

                     }
                     catch (InterruptedException e)
                     {

                     }
             }
             }
         });


    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message message)
        {
            int currentPosition = message.what;
            positionBar.setProgress(currentPosition);

            String elapsedTime =  createTimeLable(currentPosition);
            elapsedTimeLable.setText(elapsedTime);

            String remainigTime = createTimeLable(totalTime-currentPosition);
            remainingTimeLable.setText("- " + remainigTime);
        }
    };

    public String createTimeLable(int time) {
        String timeLable = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLable = min + ":";
        if (sec < 10)
            timeLable = timeLable + "0";
        timeLable += sec;

        return timeLable;
    }


    public void playBtnClick(View view)
    {
        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
            PlayButton.setBackgroundResource(R.drawable.sound);

        }
        else
        {
            mediaPlayer.pause();
            PlayButton.setBackgroundResource(R.drawable.playbutton);
        }
    }
}
