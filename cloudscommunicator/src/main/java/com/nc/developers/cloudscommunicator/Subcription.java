package com.nc.developers.cloudscommunicator;

import rx.Observable;
import rx.Observer;

public class Subcription {
    private Observable<String> mObservable;
    private Observer<String> mObserver;

    public Observable<String> getObservable(){
        return mObservable;
    }

    public void setObservable(Observable<String> mObservable){
        this.mObservable = mObservable;
    }

    public Observer<String> getObserver(){
        return mObserver;
    }

    public void setObserver(Observer<String> mObserver){
        this.mObserver = mObserver;
    }
}