package com.example.myapplication

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.view.Gravity
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog

abstract class BaseDialog(context: Context) : AlertDialog(context), DialogInterceptor {
    private var dismissListener: OnDismissListener = OnDismissListener {
        mChain?.onDismissEvent()
        dismissObserver?.onDismiss(it)
    }
    private var showListener: OnShowListener = OnShowListener {
        mChain?.onShowEvent()
        showObserver?.onShow(it)
    }
    private var dismissObserver: OnDismissListener? = null
    private var showObserver: OnShowListener? = null

    var mChain: DialogChain? = null

    /*下一个拦截器*/
    fun chain(): DialogChain? = mChain

    @CallSuper
    override fun intercept(chain: DialogChain) {
        mChain = chain
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.attributes?.width = 500
        window?.attributes?.height = 600

    }

    override fun show() {
        super.show()
        mChain?.onShowEvent()
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        this.dismissObserver = listener
        super.setOnDismissListener(dismissListener)
    }

    /*override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        this.showObserver = listener
        super.setOnShowListener(showListener)
    }*/

    /**
     *  获取window 的params 然后给params去设置x y 参数即可 我们设置的 x y 是相对值 相对自身位置的偏移量
     */
    fun setLocation(offsetX: Int, offsetY: Int) {
        if (window == null) {
            return
        }
        var layoutParams = window!!.attributes
        layoutParams.x = offsetX
        layoutParams.y = offsetY
        window?.attributes = layoutParams

        window?.setGravity(Gravity.TOP and Gravity.LEFT)
    }
}
