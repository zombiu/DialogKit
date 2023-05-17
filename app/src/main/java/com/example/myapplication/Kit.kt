package com.example.myapplication

import android.app.Fragment
import android.util.Log
import androidx.fragment.app.FragmentActivity

private fun String.logI(dialogChain: DialogChain) {
    Log.e("-->>", this)
}

class DialogChain private constructor(
    // 弹窗的时候可能需要Activity/Fragment环境。
    val activity: FragmentActivity? = null,
    val fragment: Fragment? = null,
    private var interceptors: MutableList<DialogInterceptor>?
) : DialogEventListener {

    var dialogEventListener: DialogEventListener? = null

    companion object {
        @JvmStatic
        fun create(initialCapacity: Int = 0): Builder {
            return Builder(initialCapacity)
        }

        @JvmStatic
        fun openLog(isOpen: Boolean) {
//            isOpenLog = isOpen
        }
    }

    private var index: Int = 0

    // 执行拦截器。
    fun process() {
        interceptors ?: return
        when (index) {
            in interceptors!!.indices -> {
                val interceptor = interceptors!![index]
                index++
                interceptor.intercept(this)
            }
            // 最后一个弹窗关闭的时候，我们希望释放所有弹窗引用。
            interceptors!!.size -> {
                "===> clearAllInterceptors".logI(this)
                clearAllInterceptors()
            }
        }
    }

    private fun clearAllInterceptors() {
        interceptors?.clear()
        interceptors = null
    }

    // 构建者模式。
    open class Builder(private val initialCapacity: Int = 0) {
        private val interceptors by lazy(LazyThreadSafetyMode.NONE) {
            ArrayList<DialogInterceptor>(
                initialCapacity
            )
        }
        private var activity: FragmentActivity? = null
        private var fragment: Fragment? = null
        private var dialogEventListener: DialogEventListener? = null

        // 添加一个拦截器。
        fun addInterceptor(interceptor: DialogInterceptor): Builder {
            if (!interceptors.contains(interceptor)) {
                interceptors.add(interceptor)
            }
            return this
        }

        // 关联Fragment。
        fun attach(fragment: Fragment): Builder {
            this.fragment = fragment
            return this
        }

        // 关联Activity。
        fun attach(activity: FragmentActivity): Builder {
            this.activity = activity
            return this
        }

        fun build(): DialogChain {
            var dialogChain = DialogChain(activity, fragment, interceptors)
            dialogChain.dialogEventListener = dialogEventListener
            return dialogChain
        }

        // 添加一个回调
        fun addDialogEventListener(dialogEventListener: DialogEventListener): Builder {
            this.dialogEventListener = dialogEventListener
            return this
        }
    }

    override fun onShowEvent() {
        dialogEventListener?.onShowEvent()
    }

    override fun onDismissEvent() {
        dialogEventListener?.onDismissEvent()
    }
}

open interface DialogEventListener {
    fun onShowEvent()
    fun onDismissEvent();
}

