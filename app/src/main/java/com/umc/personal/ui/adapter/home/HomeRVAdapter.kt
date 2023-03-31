package com.umc.personal.ui.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.umc.personal.R
import com.umc.personal.data.dto.home.get.HomeItem
import com.umc.personal.databinding.ItemHomeBinding

class HomeRVAdapter(private val dataList: List<HomeItem>): RecyclerView.Adapter<HomeRVAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class DataViewHolder(private val binding: ItemHomeBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HomeItem) {

            //이미지 처리
            if (data.thumbnailImage == null) {
                binding.itemImage.isVisible = false
                val layoutParams = binding.contentContainer.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.marginStart = 0
                binding.contentContainer.layoutParams = layoutParams
            } else {
                binding.itemImage.load(data.thumbnailImage)
                binding.itemImage.clipToOutline = true
            }

            //데이터 바인딩
            binding.tvTitle.text = data.title
            binding.tvContent.text = data.content
            binding.tvCommentCount.text = data.commentCount.toString()
            binding.tvLikeCount.text = data.likeCount.toString()
            binding.tvViews.text = data.view.toString()

            //프로젝트 유형 처리
            when (data.state) {
                0 -> {
                    //개인 프로젝트
                    binding.projectState.text = "개인 프로젝트"
                    binding.state.setImageResource(R.drawable.home_fragment_approval_status_approved)
                }
                1 -> {
                    //팀 프로젝트
                    binding.projectState.text = "팀 프로젝트"
                    binding.state.setImageResource(R.drawable.home_fragment_approval_status_rejected)
                }
            }

            //아이템 클릭 리스너
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listner?.onItemClick(itemView, data, pos)
                }
            }
        }
    }

    // 아이템 클릭 리스너
    interface OnItemClickListner {
        fun onItemClick(v: View, data: HomeItem, pos: Int)
    }
    private var listner: OnItemClickListner?= null

    fun setOnItemClickListener(listner: OnItemClickListner) {
        this.listner = listner
    }
}