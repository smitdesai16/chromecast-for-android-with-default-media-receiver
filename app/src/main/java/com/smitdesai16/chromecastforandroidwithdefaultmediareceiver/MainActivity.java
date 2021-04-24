package com.smitdesai16.chromecastforandroidwithdefaultmediareceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaLoadRequestData;
import com.google.android.gms.cast.MediaSeekOptions;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private SessionManager sessionManager;

    private final CastSessionListener castSessionListener = new CastSessionListener();

    private Button btnLoadImage;
    private Button btnLoadAudio;
    private Button btnLoadVideo;
    private Button btnPlayPause;
    private Button btnStop;
    private SeekBar sbProgressBar;

    private long duration = 0;

    private RemoteMediaClient.ProgressListener progressListener = new RemoteMediaClient.ProgressListener() {
        @Override
        public void onProgressUpdated(long l, long l1) {
            if(duration == 0 && l1 > 0) {
                duration = l1;
            }

            if (l1 > 0) {
                sbProgressBar.setProgress((int)(l*100/l1));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CastContext castContext = CastContext.getSharedInstance(this);
        sessionManager = castContext.getSessionManager();

        btnLoadImage = findViewById(R.id.btnLoadImage);
        btnLoadAudio = findViewById(R.id.btnLoadAudio);
        btnLoadVideo = findViewById(R.id.btnLoadVideo);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnStop = findViewById(R.id.btnStop);
        sbProgressBar = findViewById(R.id.sbProgressBar);

        btnLoadImage.setOnClickListener(this);
        btnLoadAudio.setOnClickListener(this);
        btnLoadVideo.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        sbProgressBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionManager.addSessionManagerListener(castSessionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sessionManager.removeSessionManagerListener(castSessionListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoadImage:
                try {
                    if(sessionManager.getCurrentCastSession() != null) {
                        MediaInfo mediaInfo = new MediaInfo.Builder("https://www.google.com/logos/doodles/2021/celebrating-laura-bassi-6753651837109199-l.png")
                                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                .setContentType("image/png")
                                .build();

                        MediaLoadRequestData mediaLoadRequestData = new MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).setAutoplay(true).build();

                        RemoteMediaClient remoteMediaClient = sessionManager.getCurrentCastSession().getRemoteMediaClient();
                        remoteMediaClient.load(mediaLoadRequestData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnLoadAudio:
                try {
                    if(sessionManager.getCurrentCastSession() != null) {
                        MediaInfo mediaInfo = new MediaInfo.Builder("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                .setContentType("audio/mpeg")
                                .build();

                        MediaLoadRequestData mediaLoadRequestData = new MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).setAutoplay(true).build();

                        RemoteMediaClient remoteMediaClient = sessionManager.getCurrentCastSession().getRemoteMediaClient();
                        remoteMediaClient.load(mediaLoadRequestData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnLoadVideo:
                try {
                    if(sessionManager.getCurrentCastSession() != null) {
                        MediaInfo mediaInfo = new MediaInfo.Builder("http://commondatastorage.googleapis.com/gtv-videos-bucket/big_buck_bunny_1080p.mp4")
                                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                                .setContentType("video/mp4")
                                .build();

                        MediaLoadRequestData mediaLoadRequestData = new MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).setAutoplay(true).build();

                        RemoteMediaClient remoteMediaClient = sessionManager.getCurrentCastSession().getRemoteMediaClient();
                        remoteMediaClient.load(mediaLoadRequestData);

                        remoteMediaClient.addProgressListener(progressListener, 5000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnPlayPause:
                try {
                    RemoteMediaClient remoteMediaClient = sessionManager.getCurrentCastSession().getRemoteMediaClient();
                    switch (remoteMediaClient.getPlayerState()) {
                        case MediaStatus.PLAYER_STATE_PAUSED:
                            remoteMediaClient.play();
                            break;

                        case MediaStatus.PLAYER_STATE_PLAYING:
                            remoteMediaClient.pause();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnStop:
                RemoteMediaClient remoteMediaClient = sessionManager.getCurrentCastSession().getRemoteMediaClient();
                remoteMediaClient.stop();
                sbProgressBar.setProgress(0);
                duration = 0;
                remoteMediaClient.removeProgressListener(progressListener);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.sbProgressBar:
                try {
                    RemoteMediaClient remoteMediaClient = sessionManager.getCurrentCastSession().getRemoteMediaClient();
                    MediaSeekOptions mediaSeekOptions = new MediaSeekOptions.Builder().setPosition(seekBar.getProgress() *duration/100).build();
                    remoteMediaClient.seek(mediaSeekOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}