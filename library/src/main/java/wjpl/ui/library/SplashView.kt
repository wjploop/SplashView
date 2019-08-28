package wjpl.ui.library

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.Scroller
import java.text.FieldPosition
import kotlin.math.absoluteValue

class SplashView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {

    val mData: MutableList<Int> = mutableListOf()

    val mScroller = Scroller(context)

    //当前处于的页码
    var pos = 0
        set(value) {
            field = when {
                value >= mData.size -> mData.size - 1
                value < 0 -> 0
                else -> value
            }
        }

    var mScaleType: ScaleType

    private val sScaleTypeArray = arrayOf<ScaleType>(
        ScaleType.CENTER,
        ScaleType.CENTER_CROP,
        ScaleType.CENTER_INSIDE,

        ScaleType.FIT_CENTER,
        ScaleType.FIT_END,
        ScaleType.FIT_START,
        ScaleType.FIT_XY,

        ScaleType.MATRIX
    )

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.SplashView)
        val scaleTypeIndex = a.getInt(R.styleable.SplashView_image_scale_type, 0)
        mScaleType = sScaleTypeArray[scaleTypeIndex]
        a.recycle()

    }


    val mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.d("wolf", "onScroll distantX:$distanceX")
            //处理首尾页，不让滑动超出
            if (pos == mData.size - 1) {
                if (distanceX > 0) {
                    return false
                }
            }
            if (pos == 0) {
                if (distanceX < 0) {
                    return false
                }
            }
            scrollBy(distanceX.toInt(), 0)
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.d("wolf", "cur velocityX:$velocityX")
            Log.d("wolf", "cur pos:$pos")
            if (velocityX.absoluteValue > 3) {
                if (velocityX < 0) {
                    pos++
                } else {
                    pos--
                }
                Log.d("wolf", "after fliping pos:$pos")
                smoothScrollTo(pos)

                return true
            }
            return false
        }

    })

    fun smoothScrollTo(position: Int) {
        mScroller.startScroll(scrollX, 0, pos * width - scrollX, 0)
        //请求重绘，会回调computeScroll方法
        invalidate()

    }

    fun setData(data: Iterable<Int>) {
        mData.clear()
        mData.addAll(data)


        removeAllViews()
        mData.forEach {
            ImageView(context).apply {
                setImageResource(it)
                scaleType = mScaleType
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

            }
            ACTION_UP -> {
                //启动滑动
                mScroller.startScroll(scrollX, 0, pos * width - scrollX, 0)
                //请求重绘，会回调computeScroll方法
                invalidate()

                onPageScrollListener?.let {
                    it.onPageSelected(pos)
                }
            }

        }
        return true
    }

    //在未到指定位置之前，不断计算，根据计算出来的下次的值，不断滑动
    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, 0)
            invalidate()
            onPageScrollListener?.let {
                it.onPageScrolled(mScroller.currX.toFloat() / width.toFloat(), pos)
            }
        }
    }

    var onPageScrollListener: OnPageScrollListener? = null

    interface OnPageScrollListener {
        fun onPageScrolled(offsetPercent: Float, position: Int)
        fun onPageSelected(position: Int)
    }
}