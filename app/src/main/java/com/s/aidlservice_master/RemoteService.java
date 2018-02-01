package com.s.aidlservice_master;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;

public class RemoteService extends Service {

    private static final String TAG = RemoteService.class.getSimpleName();
    //private final IBinder mBinder = new Service();

    private RemoteCallbackList<IParticipateCallback> mCallbacks = new RemoteCallbackList<>();

    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        @Override
        public void join(String name) throws RemoteException {
            notifyParticipate(name, true);

        }

        @Override
        public void leave(IBinder token) throws RemoteException {

        }

        @Override
        public List<String> getParticipators() throws RemoteException {
            return null;
        }

        @Override
        public void registerParticipateCallback(IParticipateCallback cb) throws RemoteException {

            mCallbacks.register(cb);
        }

        @Override
        public void unregisterParticipateCallback(IParticipateCallback cb) throws RemoteException {

            mCallbacks.unregister(cb);
        }
    };


    public RemoteService() {
    }

//    class Service extends Binder{
//        public RemoteService getService(){
//            return RemoteService.this;
//        }
//    }

    private void notifyParticipate(String name, boolean joinOrLeave){
        final int len = mCallbacks.beginBroadcast();
        for(int i = 0; i < len; i++){
            try{
                mCallbacks.getBroadcastItem(i).onParticipate(name, joinOrLeave);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallbacks.kill();
    }

    private final class Client implements IBinder.DeathRecipient{
        public final IBinder mToken;
        public final String mName;

        public Client(IBinder token, String name){
            this.mToken = token;
            this.mName = name;
        }
        //客户端crash，执行此回调
        @Override
        public void binderDied() {


        }
    }
}
