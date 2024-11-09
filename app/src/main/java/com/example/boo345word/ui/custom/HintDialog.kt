package com.example.boo345word.ui.custom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.boo345word.databinding.HintDialogBinding

class HintDialog(
    word: String,
) : DialogFragment() {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: HintDialogBinding? = null
    private val binding get() = _binding!!
    private var word: String? = null

    init {
        this.word = word
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        _binding = HintDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        return view
    }
}
