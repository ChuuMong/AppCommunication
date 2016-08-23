package io.chuumong.app2;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Messenger service;
    private final Messenger messenger = new Messenger(new IncomingHandler());
    private boolean isBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("io.chuumong.app1.GET_USER_ID_SERVICE");
                intent.setPackage("io.chuumong.app1");
                bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
            }
        });
    }

    private static final int REQUSER_USER_ID = 1001;
    private static final int GET_USER_ID = 2001;
    private static final String USER_ID = "USER_ID";

    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage#msg : " + msg);
            switch (msg.what) {
                case GET_USER_ID:
                    Log.d(TAG, "handleMessage#msg.getData : " + msg.getData().getString(USER_ID));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBind = true;
            service = new Messenger(iBinder);

            Log.d(TAG, "onServiceConnected");

            try {
                Message msg = Message.obtain(null, REQUSER_USER_ID);
                msg.replyTo = messenger;
                service.send(msg);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }
    }
}
