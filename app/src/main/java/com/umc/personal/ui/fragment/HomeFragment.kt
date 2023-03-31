package com.umc.personal.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.personal.data.dto.home.get.HomeItem
import com.umc.personal.databinding.FragmentHomeBinding
import com.umc.personal.ui.activity.MainActivity
import com.umc.personal.ui.activity.UploadActivity
import com.umc.personal.ui.adapter.home.HomeRVAdapter
import com.umc.personal.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.get_all_preject("")

        //검색 버튼 클릭 이벤트
        binding.search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.setQuery(binding.search.text.toString())
                    return true
                }
                return false
            }
        })

        //upload Activity로 이동
        binding.addPost.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
        }

        // query(검색어) 상태 변화시
        viewModel.query.observe(viewLifecycleOwner) {
            viewModel.get_all_preject(it)
        }

        //옵저버 패턴
        viewModel.all_list.observe(viewLifecycleOwner) {

            val dataRVAdapter = HomeRVAdapter(it.homeItem)
            binding.recentSearchRv.adapter = dataRVAdapter
            binding.recentSearchRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            // 클릭 이벤트 처리
            dataRVAdapter.setOnItemClickListener(object: HomeRVAdapter.OnItemClickListner {
                override fun onItemClick(v: View, data: HomeItem, pos: Int) {

                    //프로젝트 상세보기로 이동
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("projectId", data.projectId.toString())
                    startActivity(intent)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.get_all_preject(viewModel.query.value.toString())
    }

    //viewBinding이 더이상 필요 없을 경우 null 처리 필요
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}