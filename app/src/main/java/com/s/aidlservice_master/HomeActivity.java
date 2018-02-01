package com.s.aidlservice_master;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by 张垚杰 on 2018/1/31.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = HomeActivity.class.getSimpleName();

    private  IRemoteService mService;
    private IBinder mToken = new Binder();


    private ListView mList;
    private Button mJoinBtn;
    private Button mRegisterBtn;

    private ArrayAdapter<String> mAdapter;

    private boolean mSate = false;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(HomeActivity.this,"service connect",Toast.LENGTH_SHORT).show();
//            RemoteService.Service binder = (RemoteService.Service) service;
//            mService = binder.getService();
            mService = IRemoteService.Stub.asInterface(service);
            mSate = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(HomeActivity.this,"service disconnect",Toast.LENGTH_SHORT).show();
            mSate = false;
            mService = null;
        }
    };

    private IParticipateCallback mParticipateCallback = new IParticipateCallback.Stub() {

        @Override
        public void onParticipate(String name, boolean joinOrLeave) throws RemoteException {
            if(joinOrLeave){
                mAdapter.add(name);
            }

        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bind).setOnClickListener(this);
        findViewById(R.id.unbind).setOnClickListener(this);
        findViewById(R.id.call).setOnClickListener(this);
        findViewById(R.id.get_participators).setOnClickListener(this);

        mList = (ListView) findViewById(R.id.list);
        mJoinBtn = (Button) findViewById(R.id.join);
        mJoinBtn.setOnClickListener(this);

        mRegisterBtn = findViewById(R.id.register_callback);
        mRegisterBtn.setOnClickListener(this);

        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        mList.setAdapter(mAdapter);
    }

    private boolean isServiceReady(){
        if(mService != null){
            return true;
        }else {
            Toast.makeText(this,"Service is not available yet!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind:
                //5.0之后隐式intent需要setPackage
                Intent intent =new Intent();
                //com.zyj.service
                //package com.s.aidlservice_master;
                intent.setAction("com.zyj.service");
                intent.setPackage("com.s.aidlservice_master");
                Log.e(TAG, "PackageName: "+ getPackageName());
                //Intent intent = new Intent(this, RemoteService.class);//或者如此
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                if(mSate) {
                    unbindService(mConnection);
                    mSate = false;
                    Toast.makeText(this,"service unbind",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.call:
            case R.id.join:
                toggleJoin();
                break;
            case R.id.get_participators:
            case R.id.register_callback:
                toggleRegisterCallback();
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSate) {
            unbindService(mConnection);
            mSate = false;
        }
    }
    private void toggleRegisterCallback(){
        if(!isServiceReady()){
            return;
        }
        try{
            mService.registerParticipateCallback(mParticipateCallback);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void toggleJoin(){

        if(!isServiceReady()){
            return;
        }
        try{
            String name = "Client:"+ new Random().nextInt(10);
            mService.join(name);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
