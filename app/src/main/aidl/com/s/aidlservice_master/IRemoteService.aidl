// IRemoteService.aidl
package com.s.aidlservice_master;

import com.s.aidlservice_master.IParticipateCallback;

// Declare any non-default types here with import statements

interface IRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void join(String name);
     void leave(IBinder token);
     List<String> getParticipators();

     void registerParticipateCallback(IParticipateCallback cb);
     void unregisterParticipateCallback(IParticipateCallback cb);
}
