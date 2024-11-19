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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        _binding = HintDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.txtHintWord.text = word?.meaning
        val imageSource = "example_${word?.word}"
        val imageId = resources.getIdentifier(imageSource, "drawable", requireContext().packageName)
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        binding.imageHint.setImageResource(imageId)

        return view
    }
}
