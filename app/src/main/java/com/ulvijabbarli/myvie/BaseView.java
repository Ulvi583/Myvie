package com.ulvijabbarli.myvie;

import android.app.Activity;
import android.content.Context;

public interface BaseView<T> {

    void setPresenter(T presenter);

    Context getContextOfActivity();

    Activity getActivityOfActivity();

    void showErrorMessage(String errorMessage);

    void showProgress();

    void hideProgress();
}
