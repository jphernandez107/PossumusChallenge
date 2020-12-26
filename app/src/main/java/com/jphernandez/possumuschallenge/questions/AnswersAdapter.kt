package com.jphernandez.possumuschallenge.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jphernandez.possumuschallenge.R

class AnswersAdapter (private val onAnswerClick: (Boolean) -> Unit): ListAdapter<Pair<Boolean, String>, AnswersAdapter.ViewHolder>(
    PairDiffCallback
){

    var isAnswered = false

    class ViewHolder(view: View, val onAnswerClick: (Boolean) -> Unit): RecyclerView.ViewHolder(view) {
        val answerButton: Button = view.findViewById(R.id.answer_button)
        var isCorrectAnswer: Boolean = false
        var buttonPosition: Int = 0

        init {
            answerButton.setOnClickListener {
                onAnswerClick(isCorrectAnswer)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.answer_button_item, parent, false)
        return ViewHolder(
            view,
            onAnswerClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var color = holder.itemView.context.resources.getColor(R.color.black, null)
        if(isAnswered) {
            color = if(getItem(position).first) holder.itemView.context.resources.getColor(R.color.green, null)
            else holder.itemView.context.resources.getColor(R.color.red, null)
            holder.answerButton.setOnClickListener{}
        }
        holder.answerButton.setBackgroundColor(color)
        holder.isCorrectAnswer = getItem(position).first
        holder.answerButton.text = getItem(position).second
        holder.buttonPosition = position
    }

}

object PairDiffCallback: DiffUtil.ItemCallback<Pair<Boolean, String>>() {
    override fun areItemsTheSame(oldItem: Pair<Boolean, String>, newItem: Pair<Boolean, String>) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Pair<Boolean, String>, newItem: Pair<Boolean, String>) = oldItem.first == newItem.first && oldItem.second == newItem.second
}