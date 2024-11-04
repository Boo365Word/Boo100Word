package com.example.boo345word.ui.custom

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.viewpager2.widget.ViewPager2
import com.example.boo345word.R
import com.example.boo345word.databinding.DialogInfoViewpagerBinding

class InfoDialog(
    private val context: Context,
    private val overlay: View,
) {

    private val binding: DialogInfoViewpagerBinding = DialogInfoViewpagerBinding.inflate(
        LayoutInflater.from(context)
    )

    // 다이얼로그 생성
    private val dialog: Dialog = Dialog(context, R.style.CustomDialog).apply {
        setContentView(binding.root) // 다이얼로그 레이아웃 설정
        setCanceledOnTouchOutside(false) // 창 외부 클릭 시 안닫히게
        setCancelable(false)             // 뒤로가기 버튼으로 안닫히게
    }

    init {
        initViewPagerDialog()
        dialog.show()
    }

    private fun initViewPagerDialog() {
        // 사용할 이미지 리소스 리스트
        val images = listOf(R.drawable.info1, R.drawable.info2)

        initViewViewPagerDialogBinding(images)
        createIndicators(images.size)

    }

    private fun initViewViewPagerDialogBinding(
        @DrawableRes
        images: List<Int>,
    ) {
        with(binding) {
            // 어댑터 설정
            viewPager.adapter = InfoViewPagerAdapter(images)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateIndicators(position)
                }
            })
            // 닫기 버튼 클릭 리스너
            closeButton.setOnClickListener {
                overlay.visibility = View.GONE
                dialog.dismiss()
            }
        }
    }

    // 인디케이터 생성 함수
    private fun createIndicators(count: Int) {
        val indicators = mutableListOf<ImageView>()

        repeat(count) {
            val indicator = ImageView(context).apply {
                setImageResource(R.drawable.indicater_circle_inactive)
            }

            // 크기 설정
            val params = LinearLayout.LayoutParams(24, 24) // 크기: 24dp
            params.marginEnd = 8 // 간격 설정
            indicator.layoutParams = params
            indicators.add(indicator)
            binding.indicatorLayout.addView(indicator)
        }

        if (indicators.isNotEmpty()) {
            indicators.first()
                .setImageResource(R.drawable.indicater_circle_active) // 첫 번째 인디케이터 활성화
        }
    }

    // 인디케이터 업데이트 함수
    private fun updateIndicators(position: Int) {
        repeat(binding.indicatorLayout.childCount) { idx ->
            val indicator = binding.indicatorLayout.getChildAt(idx) as ImageView

            indicator.setImageResource(
                if (idx == position) R.drawable.indicater_circle_active else R.drawable.indicater_circle_inactive
            )
        }
    }
}