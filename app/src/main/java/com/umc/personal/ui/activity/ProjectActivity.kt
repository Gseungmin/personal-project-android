package com.umc.personal.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.umc.approval.data.dto.comment.post.CommentPostDto
import com.umc.personal.R
import com.umc.personal.databinding.ActivityProjectBinding
import com.umc.personal.ui.adapter.comment.CommentAdapter
import com.umc.personal.ui.viewmodel.ProjectViewModel
import com.umc.personal.util.Utils.categoryMap

class ProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectBinding

    private val viewModel by viewModels<ProjectViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProjectBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val projectId = intent.getStringExtra("projectId")
        viewModel.get_project_detail(projectId.toString())

        binding.cancel.setOnClickListener {
            finish()
        }

        //서버로부터 데이터 받아왔을때 View 구성
        live_data()

        //좋아요 눌렀을때 로직
        binding.heart.setOnClickListener {
            viewModel.like(projectId.toString())
        }

        //작성 누를 시 댓글 작성
        binding.writeButton.setOnClickListener {
            val postComment = CommentPostDto(projectId, binding.commentEdit.text.toString())
            viewModel.post_comments(postComment)
            binding.commentEdit.text.clear()
        }
    }

    //라이브 데이터
    private fun live_data() {

        //좋아요 라이브 데이터
        viewModel.like.observe(this) {
            if (it == true) {
                binding.heart.setImageResource(R.drawable.icon_heart_new_fill)
            } else {
                binding.heart.setImageResource(R.drawable.icon_heart_new)
            }
        }

        //프로젝트 라이브 데이터
        viewModel.project.observe(this) {

            //댓글 불러오기
            viewModel.get_comments(it.projectId.toString())

            viewModel.setLike(it.isLiked!!)
            binding.cate.text = categoryMap[it.state]
            binding.profile.load(it.profileImage)
            binding.profile.clipToOutline = true
            binding.name.text = it.nickname
            binding.title.text = it.title
            binding.content.text = it.content
            binding.image.load(it.image)
            binding.documentCommentPostLikes.text = "좋아요 " + it.likeCount.toString()
            binding.documentCommentPostViews.text = "조회수 " + it.view.toString()
            binding.documentCommentPostComments.text = "댓글 " + it.commentCount.toString()

            //링크 처리
            binding.openGraphImage.load(it.link.image)
            binding.openGraphText.text = it.link.title
            binding.openGraphUrl.text = it.link.url
        }

        //댓글 라이브 데이터
        viewModel.comments.observe(this) {

            binding.documentCommentRecyclerview.layoutManager = LinearLayoutManager(this)
            /**대댓글 다이얼로그를 위한 파라미터 로직*/
            val documentCommentAdapter = CommentAdapter(it)
            documentCommentAdapter.notifyDataSetChanged()
            binding.documentCommentRecyclerview.adapter = documentCommentAdapter

        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        binding.rowBackgroundImg.minimumHeight = binding.documentCommentPost.measuredHeight
        setContentView(binding.root)
    }
}