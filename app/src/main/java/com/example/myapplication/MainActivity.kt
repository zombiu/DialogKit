package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.example.myapplication.algorithm.Sort_Test
import com.example.myapplication.databinding.ActivityMainBinding

/**
 * 作者：陈旭学程序
链接：https://juejin.cn/post/7118258289451728909
来源：稀土掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var dialogChain: DialogChain

    private val bDialog by lazy { BDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        DialogChain.openLog(true)
        activityMainBinding.tvAnchor.post {
            createDialogChain() //创建 DialogChain
            // 开始从链头弹窗。
            dialogChain.process()
        }
        // 模拟延迟数据回调。
        Handler(Looper.getMainLooper()).postDelayed({
            bDialog.onDataCallback("延迟数据回来了！！")
        }, 5000)
        activityMainBinding.tvShow.setOnClickListener {
            Toast.makeText(this, "嘿，地道", Toast.LENGTH_LONG)
            Log.e("-->>", "嘿，地道")
        }
        activityMainBinding.content.setOnClickListener {
            Toast.makeText(this, "root 嘿，地道", Toast.LENGTH_LONG)
            Log.e("-->>", "root 嘿，地道")
        }
    }

    //创建 DialogChain
    private fun createDialogChain() {
        var aDialog = ADialog(this)
        //去掉pading方法1、设置背景 todo 不设置背景  会有一个自带的padding
        aDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //4、去掉默认padding间距 这个去除padding的 无效果
//        aDialog?.window?.getDecorView()?.setPadding(0, 0, 0, 0)
        // 设置定位原点
//        aDialog.window?.setGravity(Gravity.TOP or Gravity.START)
        var location = intArrayOf(0, 0)
        activityMainBinding.tvAnchor.getLocationOnScreen(location)
        var x = location[0] // view距离window 左边的距离（即x轴方向）
        var y = location[1] // view距离window 顶边的距离（即y轴方向）
        Log.e("-->>", "锚点view x=${x} y=${y}")
        var offsetX = x + activityMainBinding.tvAnchor.width
        var offsetY = y + (aDialog.window!!.attributes.height - activityMainBinding.tvAnchor.height) / 2
        // y轴有个状态栏的高度 需要处理
        aDialog.setLocation(offsetX, offsetY - BarUtils.getStatusBarHeight())

        var count = 0
        var first = true
        dialogChain = DialogChain.create(3)
            .attach(this)
            .addInterceptor(aDialog)
            .addInterceptor(bDialog)
            .addInterceptor(CDialog(this))
            .addDialogEventListener(object : DialogEventListener {
                override fun onShowEvent() {
                    Log.e("-->>", "onShowEvent")
                    if (first) {
                        first = false
                        // 醒目：show之后要设置原点 否则定位会出错
                        aDialog.window?.setGravity(Gravity.TOP or Gravity.START)
                    }
                }

                override fun onDismissEvent() {
                    Log.e("-->>", "onDismissEvent")
                    count++
                    if (count == 3) {
                        Log.e("-->>", "onDismissEvent 恢复点击")
                        activityMainBinding.vCover.isClickable = false
                    }
                }

            })
            .build()


        var sortTest = Sort_Test()
        LogUtils.e("-->>", "返回的索引=${sortTest.findFirst(5)}")

    }

    override fun onStart() {
        super.onStart()
        activityMainBinding.vCover.isClickable = true
    }
}

