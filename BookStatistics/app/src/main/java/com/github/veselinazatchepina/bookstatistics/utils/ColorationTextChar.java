package com.github.veselinazatchepina.bookstatistics.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.github.veselinazatchepina.bookstatistics.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class helps set specific color first vowel in title
 */
public class ColorationTextChar {

    public static Spannable setFirstVowelColor(String text, Context context) {
        Spannable newText = new SpannableString(text);
        if (!text.isEmpty() || !text.equals("")) {
            int index = getFirstVowelIndex(text);
            newText = new SpannableString(text);
            if (index != -1) {
                newText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.book_accent_background)),
                        index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return newText;
        }
        return newText;
    }

    /**
     * Method returns index of first vowel in title (for english languages)
     *
     * @param text     current text
     * @return index of first vowel
     */
    private static int getFirstVowelIndex(String text) {
        String patternString = "(?i:[aeiouy]).*";
        return getIndex(patternString, text);
    }

    private static int getIndex(String patternString, String text) {
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.start();
        } else {
            return -1;
        }
    }
}
