package com.testsensor.pc;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastWithTwoText {
    private static ToastWithTwoText toastWithTwoText;

    private Toast toast;
    private Context mContext;

    private ToastWithTwoText(Context context) {
        this.mContext = context;
    }

    public static ToastWithTwoText createToastConfig(Context context) {
        if (toastWithTwoText == null) {
            toastWithTwoText = new ToastWithTwoText(context);
        }
        return toastWithTwoText;
    }

    /**
     * 显示Toast
     *
     * @param tvStrOne
     * @param tvStrTwo
     */

    public void ToastShow(String tvStrOne, String tvStrTwo) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.layout_toast_with_two_text, null);
        TextView tvOne = (TextView) layout.findViewById(R.id.tv_text_one);
        TextView tvTwo = (TextView) layout.findViewById(R.id.tv_text_two);
        tvOne.setText(tvStrOne);
        tvTwo.setText(tvStrTwo);
        toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void ToastShow(int idStrOne, int idStrTwo) {
        ToastShow(mContext.getString(idStrOne), mContext.getString(idStrTwo));
    }

}
