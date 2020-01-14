package vn.harry.callrecorder.util;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StringUtils {

    public static boolean isEmpty(String input) {
        return (input == null || input.equals("null") || input.length() == 0);
    }

    @SuppressWarnings("deprecation")
    public static Spanned getContentFromHtmlString(String input) {
        if (isEmpty(input)) {
            return new SpannableString("");
        }
        return Html.fromHtml(input);
    }

    public static void setText(String input, TextView textView) {
        setText(input, textView, false);
    }

    public static void setText(String input, TextView textView, boolean fromHtml) {
        if (textView == null) {
            return;
        }
        input = getNullableString(input);
        if (fromHtml) {
            textView.setText(getContentFromHtmlString(input));
        } else {
            textView.setText(getNullableString(input));
        }

        textView.setVisibility(isEmpty(textView.getText().toString()) ? View.GONE : View.VISIBLE);
    }

    public static void setText(String input, Button textView) {
        if (textView == null) {
            return;
        }
        textView.setText(getNullableString(input));
    }

    public static String getNullableString(String input) {
        return isEmpty(input) ? "" : input;
    }

}
