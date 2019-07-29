package com.ulvijabbarli.myvie.api;


import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.ulvijabbarli.myvie.BaseView;
import com.ulvijabbarli.myvie.data.pojo.ErrorPOJO;
import com.ulvijabbarli.myvie.util.HttpResponseHandlerUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import okhttp3.ResponseBody;
import retrofit2.Response;


public abstract class BaseSubscriber <R extends Object> implements Subscriber<R>, Observer<R> {


    private BaseView view;

    public BaseSubscriber() {
    }

    public BaseSubscriber(BaseView view) {
        this.view = view;
    }

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(R r) {

    }

    @Override
    public void onError(Throwable e) {
        if (view == null) {
            return;
        }
        view.hideProgress();
        try {
            Gson gson = new Gson();
            if (e instanceof HttpException) {
                Response response = ((HttpException) e).response();
                ResponseBody body = response.errorBody();
                if (response.code() == 403) {
                    ErrorPOJO errorPOJO = new ErrorPOJO();
                    List<String> errors = new ArrayList<>();
                    errors.add(response.errorBody().string());
                    errorPOJO.setErrors(errors);
                    HttpResponseHandlerUtil.onAPIError(view, errorPOJO, e);
                } else {
                    ErrorPOJO errorPOJO = gson.fromJson(body.string(), ErrorPOJO.class);
                    HttpResponseHandlerUtil.onAPIError(view, errorPOJO, e);
                }
            } else {
                HttpResponseHandlerUtil.onAPIError(view, null, e);
            }
        } catch (Exception ex) {
            HttpResponseHandlerUtil.onAPIError(view, null, ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        if (view == null) {
            return;
        }

        view.hideProgress();
    }
}
