package wjpl.ui.splashview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import wjpl.ui.library.SplashView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)


        val images = listOf(
            R.mipmap.pic_1,
            R.mipmap.pic_2,
            R.mipmap.pic_3,
            R.mipmap.pic_4,
            R.mipmap.pic_5,
            R.mipmap.pic_6
        )

        splash_view.setData(images)
        splash_view.onPageScrollListener = object : SplashView.OnPageScrollListener {
            override fun onPageScrolled(offsetPercent: Float, position: Int) {
            }

            override fun onPageSelected(position: Int) {
                Log.d("wolf","onPageSelected position:$position")

            }
        }
    }
}
