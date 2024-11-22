package com.example.boo345word.ui.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.boo345word.R
import com.example.boo345word.data.entity.BasicWord

class GameResultWrongListAdapter(
    private val wrongWordList: List<BasicWord>
) : BaseAdapter() {
    override fun getCount(): Int = wrongWordList.size

    override fun getItem(position: Int): Any = wrongWordList[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: View =
            LayoutInflater.from(parent?.context).inflate(R.layout.game_result_word_item, null)
        val wordName = view.findViewById<TextView>(R.id.txt_word_name)
        val wordMeaning = view.findViewById<TextView>(R.id.txt_word_meaning)

        wordName.text = wrongWordList[position].word.replace('_', ' ')
        wordMeaning.text = wrongWordList[position].meaning

        return view
    }
}
