package wjpl.ui.library

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Scroller

class SplashView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {

    val mData: MutableList<Int> = mutableListOf()

    val mScroller = Scroller(context)

    var pos = 0

    val mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            scrollBy(distanceX.toInt(), 0)
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    })

    fun setData(data: Iterable<Int>) {
        mData.clear()
        mData.addAll(data)


        removeAllViews()
        mData.forEach {
            ImageView(context).apply {
                setImageResource(it)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.let {
                addView(it)
            }
        }
        invalidate()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(i * width, 0, (i + 1) * width, b)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(event)
        when (event.action) {
            ACTION_DOWN -> {

            }
            ACTION_MOVE -> {
                //确定当前处于第几页
                pos = (scrollX + width / 2) / width
                if (pos < 0) {
                    pos = 0
                } else if (pos > mData.size - 1) {
                    pos = mData.size - 1
                }
            }
            ACTION_UP -> {
                //启动滑动
                mScroller.startScroll(scrollX, 0, pos * width - scrollX, 0)
                //请求重绘，会回调computeScroll方法
                invalidate()
            }

        }
        return true
    }

    //在未到指定位置之前，不断计算，根据计算出来的下次的值，不断滑动
    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, 0)
            invalidate()
        }
    }
}