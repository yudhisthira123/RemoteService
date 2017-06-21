package com.example.yudhisthira.remoteservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yudhisthira on 21/06/17.
 */

public class RemoteService extends Service {
    private static final String TAG = RemoteService.class.getSimpleName();

    private List<Messenger> clientList = new LinkedList<>();

    private static final int MSG_REGISTER_CLIENT = 1;
    private static final int MSG_UNREGISTER_CLIENT = 2;
    private static final int MSG_CLIENT = 3;

    class IncommingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MSG_REGISTER_CLIENT:
                    Log.d(TAG, "MSG_REGISTER_CLIENT");
                    clientList.add(msg.replyTo);
                    break;

                case MSG_UNREGISTER_CLIENT:
                    Log.d(TAG, "MSG_UNREGISTER_CLIENT");
                    clientList.remove(msg.replyTo);
                    break;

                case MSG_CLIENT:
                    Log.d(TAG, "MSG_CLIENT");

                    try {

                        Message message = Message.obtain();
                        for(int i = 0 ; i < clientList.size() ; i++){
                            clientList.get(i).send(message);
                        }
                        messenger.send(message);

                    }
                    catch (RemoteException e) {

                        Log.d(TAG, "RemoteException in IncommingHandler");
                        e.printStackTrace();
                    }

                    break;

                    default:
                        super.handleMessage(msg);

            }

            Log.d(TAG, "handleMessage");
        }
    }

    private Messenger messenger = new Messenger(new IncommingHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

}
