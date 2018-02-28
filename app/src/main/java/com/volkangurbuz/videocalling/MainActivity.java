package com.volkangurbuz.videocalling;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener{


    private static String API_KEY = "api_key here";
    private static String SESSION_ID = "session id here";
    private static String TOKEN = "token here";
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int RC_setting = 123;

    private FrameLayout PublisherContainer, SubsContainer;

    private Session session;

    private Publisher publisher;
    private Subscriber subscriber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        PublisherContainer = findViewById(R.id.publisher_container);
        SubsContainer = findViewById(R.id.subs_container);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);

    }

    @AfterPermissionGranted(RC_setting)
    public void requestPermission(){
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(this,perm)){

            session = new Session.Builder(this,API_KEY,SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
        }

        else{
            EasyPermissions.requestPermissions(this, "this app needs to access your camera and etc",RC_setting,perm);

        }

    }


    @Override
    public void onConnected(Session session) {

        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        PublisherContainer.addView(publisher.getView());
        session.publish(publisher);

    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        if (subscriber == null){

            subscriber = new Subscriber.Builder(this,stream).build();
            session.subscribe(subscriber);
            SubsContainer.addView(subscriber.getView());
        }


    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        if (subscriber != null){

            subscriber = null;
            SubsContainer.removeAllViews();

        }



    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
