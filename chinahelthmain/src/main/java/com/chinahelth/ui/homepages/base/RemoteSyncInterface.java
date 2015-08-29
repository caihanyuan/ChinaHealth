package com.chinahelth.ui.homepages.base;

/**
 * Created by caihanyuan on 15-7-14.
 */
public interface RemoteSyncInterface {

    void setRemoteSyncListner(RemoteSyncListenr remoteSyncListner);

    void loadDataInDatabase();

    void syncDataFromRemote();

    interface RemoteSyncListenr {
        void onLoadDataInDatabaseStart();

        void onLoadDataInDatabaseComplete();

        void onSyncDataFromRemoteStart();

        void onSyncDataFromRemoteComplete();
    }
}
