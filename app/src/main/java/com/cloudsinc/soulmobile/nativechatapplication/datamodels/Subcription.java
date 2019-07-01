package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import rx.Observable;
import rx.Observer;

/**
 * Created by developers on 4/5/18.
 */

public class Subcription {

    public static Observer getObserver() {
        return observer;
    }

    public static void setObserver(Observer observer) {
        Subcription.observer = observer;
    }

    public static Observer getSignupObserver() {
        return signupObserver;
    }

    public static void setSignupObserver(Observer observer) {
        Subcription.signupObserver = observer;
    }

    static Observer observer, signupObserver;

    public static Observable getObservable() {
        return observable;
    }

    public static void setObservable(Observable observable) {
        Subcription.observable = observable;
    }

    public static Observable getSignupObservable() {
        return signupObservable;
    }

    public static void setSignupObservable(Observable observable) {
        Subcription.signupObservable = observable;
    }

    static Observable observable, signupObservable;
}