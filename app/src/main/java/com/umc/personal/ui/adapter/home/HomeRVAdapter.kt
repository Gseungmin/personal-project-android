package com.umc.personal.ui.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.personal.R
import com.umc.personal.data.dto.home.HomeItem
import com.umc.personal.data.dto.home.HomeItemDto
import com.umc.personal.databinding.ItemHomeBinding

class HomeRVAdapter(private val dataList: HomeItemDto): RecyclerView.Adapter<HomeRVAdapter.DataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList.homeItem[position])
    }

    override fun getItemCount(): Int = dataList.homeItem.size

    inner class DataViewHolder(private val binding: ItemHomeBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        val context = context
        fun bind(data: HomeItem) {
//
//            /**
//             * 이미지가 없으면 이미지 제외하고 처리
//             * 이미지가 있으면 로드
//             * */
//            if (data.thumbnailImage == null) {
//                binding.itemImage.isVisible = false
//                binding.tvImageCount.isVisible = false
//                val layoutParams = binding.contentContainer.layoutParams as ConstraintLayout.LayoutParams
//                layoutParams.marginStart = 0
//                binding.contentContainer.layoutParams = layoutParams
//            } else {
//                binding.itemImage.load(data.thumbnailImage)
//                binding.itemImage.clipToOutline = true
//            }
//
//            /**
//             * 제목, 내용, 승인, 거절, 뷰, 카테고리, 작성시간표시
//             * */
//            binding.tvTitle.text = data.title
//            binding.tvContent.text = data.content
//            binding.tvApproveCount.text = data.approvalCount.toString()
//            binding.tvRejectCount.text = data.rejectCount.toString()
//            binding.tvViews.text = data.view.toString()
//            binding.tvCategory.text = categoryMap[data.category]
//            binding.tvWriteTime.text = data.datetime
//            binding.tvImageCount.text = data.imageCount.toString()

            /**승인 상태에 따라 처리
             * */
            when (data.state) {
                0 -> {
                    // 구현 중
                    binding.approvalPaperBackground.setImageResource(R.drawable.approval_fragment_approved_paper_background)
                    binding.projectState.text = "구현중"
                    binding.state.setImageResource(R.drawable.home_fragment_approval_status_approved)
                }
                1 -> {
                    // 운영 중
                    binding.approvalPaperBackground.setImageResource(R.drawable.approval_fragment_rejected_paper_background)
                    binding.projectState.text = "운영중"
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