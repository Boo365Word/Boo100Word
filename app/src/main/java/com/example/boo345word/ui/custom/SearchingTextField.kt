package com.example.boo345word.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import com.example.boo345word.databinding.SearchingTextFieldBinding

class SearchingTextField(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val binding = SearchingTextFieldBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    ).also { binding ->
        binding.btnInitSeraching.setOnClickListener {
            binding.etVocaSearching.setText("")
        }
    }

    fun setOnSearchingTextFieldListener(onTextChanged: (String) -> Unit) {
        binding.etVocaSearching.doAfterTextChanged { keyWord ->
            onTextChanged(keyWord.toString())
        }
    }
}