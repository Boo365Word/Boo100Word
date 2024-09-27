package com.example.boo345word.ui.custom

import android.app.Dialog
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.boo345word.R

class InfoDialog (private val activity: AppCompatActivity) {

    // 다이얼로그 페이지 함수
    fun showViewPagerDialog(overlay: View) {
        // 다이얼로그 생성
        val dialog = Dialog(activity, R.style.CustomDialog)
        dialog.setContentView(R.layout.dialog_info_viewpager) // 다이얼로그 레이아웃 설정

        dialog.setCanceledOnTouchOutside(false) // 창 외부 클릭 시 안닫히게
        dialog.setCancelable(false)             // 뒤로가기 버튼으로 안닫히게

        // ViewPager2와 인디케이터 레이아웃 참조
        val viewPager: ViewPager2 = dialog.findViewById(R.id.viewPager)
        val indicatorLayout: LinearLayout = dialog.findViewById(R.id.indicatorLayout)
        val closeButton: ImageView = dialog.findViewById(R.id.closeButton)

        // 사용할 이미지 리소스 리스트
        val images = listOf(R.drawable.info1, R.drawable.info2)

        // 어댑터 설정
        val adapter = InfoViewPagerAdapter(images)
        viewPager.adapter = adapter

        // 동적 인디케이터 생성
        createIndicators(indicatorLayout, images.size)

        // ViewPager 페이지 변경 시 인디케이터 업데이트
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(indicatorLayout, position)
            }
        })

        // 닫기 버튼 클릭 리스너
        closeButton.setOnClickListener {
            overlay.visibility = View.GONE
            dialog.dismiss() // 다이얼로그 닫기
        }

        dialog.show()
    }

    // 인디케이터 생성 함수
    private fun createIndicators(indicatorLayout: LinearLayout, count: Int) {
        val indicators = mutableListOf<ImageView>()
        for (i in 0 until count) {
            val indicator = ImageView(activity)
            indicator.setImageResource(R.drawable.indicater_circle_inactive) // 비활성 상태

            // 크기 설정
            val params = LinearLayout.LayoutParams(24, 24) // 크기: 24dp
            params.marginEnd = 8 // 간격 설정
            indicator.layoutParams = params
            indicators.add(indicator)
            indicatorLayout.addView(indicator)
        }

        if (indicators.isNotEmpty()) {
            indicators[0].setImageResource(R.drawable.indicater_circle_active) // 첫 번째 인디케이터 활성화
        }
    }

    // 인디케이터 업데이트 함수
    private fun updateIndicators(indicatorLayout: LinearLayout, position: Int) {
        for (i in 0 until indicatorLayout.childCount) {
            val indicator = indicatorLayout.getChildAt(i) as ImageView
            indicator.setImageResource(
                if (i == position) R.drawable.indicater_circle_active else R.drawable.indicater_circle_inactive
            )
        }
    }
}