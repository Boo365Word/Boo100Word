package com.lion.boo100word.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import com.lion.boo100word.databinding.SearchingTextFieldBinding

class SearchingTextField(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val binding = SearchingTextFieldBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    ).also { binding ->
        binding.btnInitSearching.setOnClickListener {
            binding.etWordSearching.setText("")
        }
    }

    fun setOnSearchingTextFieldListener(onTextChanged: (String) -> Unit) {
        binding.etWordSearching.doAfterTextChanged { keyWord ->
            onTextChanged(keyWord.toString())
        }
    }
}
