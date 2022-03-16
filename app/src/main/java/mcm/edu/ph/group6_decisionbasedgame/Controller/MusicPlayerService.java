package mcm.edu.ph.group6_decisionbasedgame.Controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import mcm.edu.ph.group6_decisionbasedgame.R;

public class MusicPlayerService extends Service {
    MediaPlayer player;
    private final IBinder mBinder = new MyBinder();
    private Boolean mIsPaused;
    public int currentTrack;

    public MusicPlayerService() {
    }

    public void onCreate(){
        super.onCreate();
        mIsPaused=true;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public MusicPlayerService getService(){
            return MusicPlayerService.this;
        }
    }


    public void playMusic(final int music){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(music == 1) playMainMusic();
            }
        }).start();
    }

    public void playMainMusic(){
        if(player !=null){
            player.stop();
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.horrormusic1);
        currentTrack = 1;
        player.setLooping(true);
        player.start();
    }

    public void pauseMusic(){
        player.pause();
        mIsPaused=true;
    }

    public void unpauseMusic(){
        if (!player.isPlaying()){
            player.start();
            mIsPaused=false;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

}
