package com.example.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog

abstract class BaseDialog(context: Context) : AlertDialog(context), DialogInterceptor {
    private var dismissListener: OnDismissListener = OnDismissListener {
        mChain?.onDismissEvent()
        dismissObserver?.onDismiss(it)
    }
    private var dismissObserver: OnDismissListener? = null

    var mChain: DialogChain? = null

    /*下一个拦截器*/
    fun chain(): DialogChain? = mChain

    @CallSuper
    override fun intercept(chain: DialogChain) {
        mChain = chain
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.attributes?.width = 800
        window?.attributes?.height = 900

    }

    override fun show() {
        super.show()
        mChain?.onShowEvent()
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        this.dismissObserver = listener
        super.setOnDismissListener(dismissListener)
    }
}
