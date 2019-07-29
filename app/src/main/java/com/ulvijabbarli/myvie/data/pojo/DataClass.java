package com.ulvijabbarli.myvie.data.pojo;

import io.reactivex.Observable;

public class DataClass {

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public Observable<String> valueObservable() {
        return Observable.defer(() -> Observable.just(value));
    }
}
