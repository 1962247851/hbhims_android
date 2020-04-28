package com.example.hbhims.view.custom

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.example.hbhims.R
import org.jetbrains.annotations.NotNull

/**
 * LoadingDialog
 *
 * @author qq1962247851
 * @date 2020/2/18 14:19
 */
class LoadingDialog(context: Context) : AlertDialog(context) {

    private var textView: TextView

    override fun dismiss() {
        if (dialog != null) {
            dialog = null
        }
        super.dismiss()
    }

    companion object {
        private var dialog: LoadingDialog? = null

        @JvmStatic
        fun with(context: Context): LoadingDialog {
            if (dialog == null) {
                dialog = LoadingDialog(context)
            }
            return dialog!!
        }
    }

    fun setMessage(@NotNull message: String): LoadingDialog {
        textView.text = message
        return this
    }

    fun setMessage(@StringRes resId: Int): LoadingDialog {
        textView.text = context.getText(resId)
        return this
    }

    init {
        setCancelable(false)
        val inflate = View.inflate(context, R.layout.loading_dialog, null)
        setView(inflate)
        textView = inflate.findViewById(R.id.text_view)
    }
}