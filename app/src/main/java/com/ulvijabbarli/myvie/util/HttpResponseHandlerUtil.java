package com.ulvijabbarli.myvie.util;

import com.ulvijabbarli.myvie.BaseView;
import com.ulvijabbarli.myvie.data.pojo.ErrorPOJO;

public class HttpResponseHandlerUtil {

    public static void onAPIError(BaseView view, ErrorPOJO errorPOJO, Throwable t) {
        if (view == null) {
            return;
        }

        if (errorPOJO != null) {
            handleErrorWithApiMessage(view, errorPOJO);
            return;
        }

        if (t != null) {
            handleThrowableError(view, t);
        }
    }

    private static void handleThrowableError(BaseView view, Throwable t) {
        if (t != null) {
            view.showErrorMessage(t.getMessage());
        }
    }

    private static void handleErrorWithApiMessage(BaseView view, ErrorPOJO errorPOJO) {
        StringBuilder builder = new StringBuilder();
        for (String errorText : errorPOJO.getErrors()) {
            builder.append(errorText).append(" ");
        }
        view.showErrorMessage(builder.toString());
    }

}
