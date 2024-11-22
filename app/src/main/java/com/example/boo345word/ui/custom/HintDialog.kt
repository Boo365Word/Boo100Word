package com.example.boo345word.ui.custom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.boo345word.data.entity.BasicWord
import com.example.boo345word.databinding.HintDialogBinding

class HintDialog(
    word: BasicWord
) : DialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: HintDialogBinding? = null
    private val binding get() = _binding!!
    private var word: BasicWord? = null

    init {
        this.word = word
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HintDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.let { window ->
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHintDialogView()
    }

    private fun initHintDialogView() {
        val imageSource = "${word?.word}"
        val imageId = resources.getIdentifier(imageSource, "drawable", requireContext().packageName)

        with(binding) {
            txtHintWord.text = word?.meaning
            btnClose.setOnClickListener { dismiss() }
            imageHint.setImageResource(imageId)
        }
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }
}
