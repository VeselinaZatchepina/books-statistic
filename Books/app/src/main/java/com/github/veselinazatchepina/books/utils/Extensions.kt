package com.github.veselinazatchepina.books.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.github.veselinazatchepina.books.R
import java.util.*
import java.util.regex.Pattern


/**
 * Method sets color of first vowel char. Color is accent color.
 */
fun String.setFirstVowelColor(context: Context): Spannable {
    var newText: Spannable = SpannableString(this)
    if (!this.isEmpty() || this != "") {
        val index = getFirstVowelIndex(this)
        newText = SpannableString(this)
        if (index != -1) {
            newText.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.cardBackground)),
                    index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return newText
    }
    return newText
}

/**
 * Method returns index of first vowel in title (for en and ru languages)
 *
 * @param text     current text
 * @return index of first vowel char
 */
private fun getFirstVowelIndex(text: String): Int {
    var patternString = ""
    when (Locale.getDefault().language) {
        "en" -> patternString = "(?i:[aeiouy]).*"
        "ru" -> patternString = "(?i:[аоиеёэыуюя]).*"
    }
    return getIndex(patternString, text)
}

/**
 * Fun returns index of first vowel char
 *
 * @param patternString pattern for searching first vowel char
 * @param text current text
 */
private fun getIndex(patternString: String, text: String): Int {
    val p = Pattern.compile(patternString)
    val m = p.matcher(text)
    return if (m.find()) {
        m.start()
    } else {
        -1
    }
}