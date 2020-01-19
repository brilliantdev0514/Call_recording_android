package com.smart.callrec.mvp;

public interface Presenter<V extends BaseMvpView> {

    void attachView(V mvpView);

    void detachView();
}
