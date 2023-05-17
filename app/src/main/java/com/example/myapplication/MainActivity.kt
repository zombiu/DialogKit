package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

/**
 * 作者：陈旭学程序
链接：https://juejin.cn/post/7118258289451728909
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var dialogChain: DialogChain

    private val bDialog by lazy { BDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DialogChain.openLog(true)
        createDialogChain() //创建 DialogChain
        // 模拟延迟数据回调。
        Handler(Looper.getMainLooper()).postDelayed({
            bDialog.onDataCallback("延迟数据回来了！！")
        }, 50000)
    }

    //创建 DialogChain
    private fun createDialogChain() {
        dialogChain = DialogChain.create(3)
            .attach(this)
            .addInterceptor(ADialog(this))
            .addInterceptor(bDialog)
            .addInterceptor(CDialog(this))
            .build()

    }

    override fun onStart() {
        super.onStart()
        // 开始从链头弹窗。
        dialogChain.process()
    }
}
