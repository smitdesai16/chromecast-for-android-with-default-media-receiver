package com.smitdesai16.chromecastforandroidwithdefaultmediareceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.SessionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SessionManager sessionManager;

    private final CastSessionListener castSessionListener = new CastSessionListener();

    private Button btnLoadImage;
    private Button btnLoadAudio;
    private Button btnLoadVideo;
    private Button btnPlayPause;
    private Button btnStop;

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

        btnLoadImage.setOnClickListener(this);
        btnLoadAudio.setOnClickListener(this);
        btnLoadVideo.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
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
                break;

            case R.id.btnLoadAudio:
                break;

            case R.id.btnLoadVideo:
                break;

            case R.id.btnPlayPause:
                break;

            case R.id.btnStop:
                break;
        }
    }
}