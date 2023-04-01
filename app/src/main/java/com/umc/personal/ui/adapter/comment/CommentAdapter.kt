package com.umc.personal.ui.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.personal.data.dto.comment.get.CommentDto
import com.umc.personal.data.dto.comment.get.CommentListDto
import com.umc.personal.databinding.DocumentCommentRecyclerviewItemBinding

class CommentAdapter(val itemList : CommentListDto) : RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: DocumentCommentRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binding(data: CommentDto) {

            binding.documentCommentItemProfilepic.load(data.profileImage)
            binding.documentCommentItemName.text = data.nickname
            binding.documentCommentItemContent.text = data.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = DocumentCommentRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(itemList.content[position])
    }

    override fun getItemCount(): Int {
        return itemList.content.size
    }
}