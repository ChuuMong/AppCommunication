package io.chuumong.app1;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by LeeJongHun on 2016-08-23.
 */
public class UserIdService extends Service {

    private ArrayList<Messenger> messengers = new ArrayList<>();
    final Messenger messenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private static final int REQUSER_USER_ID = 1001;
    private static final int GET_USER_ID = 2001;
    private static final String USER_ID = "USER_ID";

    class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUSER_USER_ID:
                    try {
                        Message message = Message.obtain(null, GET_USER_ID);
                        Bundle bundle = new Bundle();
                        bundle.putString(USER_ID, "test user id");
                        message.setData(bundle);
                        
                        msg.replyTo.send(message);
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
