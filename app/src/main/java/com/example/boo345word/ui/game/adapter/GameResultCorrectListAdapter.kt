package com.example.boo345word.ui.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.boo345word.R
import com.example.boo345word.data.entity.BasicWord

class GameResultCorrectListAdapter(
    private val correctWordList: List<BasicWord>,
) : BaseAdapter() {
    override fun getCount(): Int = correctWordList.size

    override fun getItem(position: Int): Any = correctWordList[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?,
    ): View {
        val view: View =
            LayoutInflater.from(parent?.context).inflate(R.layout.game_result_word_item, null)
        val wordName = view.findViewById<TextView>(R.id.txt_word_name)
        val wordMeaning = view.findViewById<TextView>(R.id.txt_word_meaning)

        wordName.text = correctWordList[position].word
        wordMeaning.text = correctWordList[position].meaning

        return view
    }
}
