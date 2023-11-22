package com.phuoc.crudstudent.AndroidUtils;



import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

public class AndroidUtils {
    public static boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void hideKeyboard(Context context) {
        if (context == null) {
            return;
        }

        // Lấy ra View được focus
        View focusedView = null;
        if (context instanceof Activity) {
            focusedView = ((Activity) context).getCurrentFocus();
        }

        // Ẩn bàn phím nếu có View được focus
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }
}
