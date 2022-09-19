package com.siroca.todo09.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.siroca.todo09.room.BoardModel
import com.siroca.todo09.ItemClick
import com.siroca.todo09.databinding.ItemBoardBinding

class PagerAdapter(private val list: ArrayList<BoardModel>, private val listener: ItemClick) :
    RecyclerView.Adapter<PagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class PagerViewHolder(private val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(model: BoardModel) {
            with(binding) {
                title.text = model.title
                nextBtn.text = model.buttonText
                nextBtn.setOnClickListener {
                    if (model.buttonText == "Начинаем") {
                        listener.onClick()
                    }
                }
            }
        }
    }
}