// IParticipateCallback.aidl
package com.s.aidlservice_master;

// Declare any non-default types here with import statements

interface IParticipateCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     //加入或者离开的回调
     void onParticipate(String name, boolean joinOrLeave);
}
