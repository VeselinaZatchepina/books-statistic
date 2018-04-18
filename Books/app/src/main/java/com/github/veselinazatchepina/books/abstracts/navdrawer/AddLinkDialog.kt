package com.github.veselinazatchepina.books.abstracts.navdrawer

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.github.veselinazatchepina.books.R
import kotlinx.android.synthetic.main.dialog_add_link.view.*


class AddLinkDialog : DialogFragment() {

    private val navDrawerViewModel: NavDrawerViewModel by lazy {
        ViewModelProviders.of(activity!!).get(NavDrawerViewModel::class.java)
    }

    companion object {
        fun newInstance(): AddLinkDialog {
            return AddLinkDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_add_link,
                null
        )
        defineSaveDialogButton(dialogView)
        defineCancelDialogButton(dialogView)
        return defineAlterDialogBuilder(dialogView).create()
    }

    private fun defineSaveDialogButton(dialogView: View) {
        dialogView.saveLinkButton.setOnClickListener {
            if (!isFieldEmpty(dialogView.addEmailText, dialogView.addEmailTextInputLayout) &&
                    !isFieldEmpty(dialogView.addPasswordText, dialogView.addPasswordTextInputLayout) &&
                    !isFieldEmpty(dialogView.addPasswordAgainText, dialogView.addPasswordAgainTextInputLayout)) {
                if (dialogView.addPasswordText.text.toString() == dialogView.addPasswordAgainText.text.toString()) {
                    navDrawerViewModel.linkUserWithEmailAuth(dialogView.addEmailText.text.toString(),
                            dialogView.addPasswordText.text.toString())
                    dismiss()
                } else {
                    dialogView.addPasswordTextInputLayout.error = "Passwords don't match"
                }
            }
        }
    }

    private fun defineCancelDialogButton(dialogView: View) {
        dialogView.cancelLinkButton.setOnClickListener {
            dismiss()
        }
    }

    private fun defineAlterDialogBuilder(dialogView: View): AlertDialog.Builder {
        return AlertDialog.Builder(activity!!).apply {
            setView(dialogView)
            setTitle(getString(R.string.add_account_title))
            setCancelable(false)
        }
    }

    private fun isFieldEmpty(editText: EditText, textInputLayout: TextInputLayout) =
            if (TextUtils.isEmpty(editText.text)) {
                textInputLayout.error = "This field couldn't be empty"
                true
            } else {
                false
            }
}