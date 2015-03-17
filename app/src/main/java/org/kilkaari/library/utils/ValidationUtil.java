package org.kilkaari.library.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * The Class ValidationUtil.
 */
public class ValidationUtil {

    /**
     * Checks if is error present.
     *
     * @param editText the edit text
     * @return true, if is error present
     */
    public static boolean isErrorPresent(EditText editText) {
        return !TextUtils.isEmpty(editText.getError());
    }



    /**
     * Empty validation.
     *
     * @param context  the context
     * @param editText the edit text
     * @param msgId    the msg id
     */
    public static void emptyValidation(Context context, EditText editText,
                                       int msgId) {
        String msg = context.getResources().getString(msgId);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(msg);
            return;
        }
        editText.setError(null);

    }

    public static void compareValidation(Context context, EditText compareWith,EditText compareText,
                                       int msgId) {
        String msg = context.getResources().getString(msgId);

        if (compareText.getText().toString().equals(compareWith.getText().toString())) {

            compareText.setError(null);
        }
            else
        {
            compareText.setError(msg);
            return;
        }



    }

    /**
     * Checks if is valid email.
     *
     * @param target the target
     * @return true, if is valid email
     */
    public final static boolean isValidEmail(CharSequence target) {
        try {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        } catch (NullPointerException exception) {
            return false;
        }
    }

    /**
     * Email validation.
     *
     * @param context  the context
     * @param editText the edit text
     * @param msgId    the msg id
     */
    public final static void emailValidation(Context context,
                                             EditText editText, int msgId) {
        String target = editText.getText().toString();
        boolean bool = false;
        try {
            bool = android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();

        } catch (NullPointerException exception) {
            bool = false;
        }
        if (!bool) {
            String msg = context.getResources().getString(msgId);
            editText.setError(msg);
        }
    }
}
