package com.siroca.todo09.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.siroca.todo09.room.BoardModel
import com.siroca.todo09.ItemClick
import com.siroca.todo09.adapter.PagerAdapter
import com.siroca.todo09.R
import com.siroca.todo09.databinding.FragmentOnBoardBinding

class OnBoardFragment : Fragment(), ItemClick {

    private lateinit var binding: FragmentOnBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPager()
        checkIsShow()
    }

    private fun checkIsShow() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("board_preference", Context.MODE_PRIVATE)
        val isShown: Boolean = sharedPreferences.getBoolean("isShow", false)
        if (isShown) {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun setupPager() {
        val list = arrayListOf<BoardModel>()
        list.add(BoardModel(R.drawable.board_first, "Экономь время", "Дальше"))
        list.add(BoardModel(R.drawable.board_second, "Достигай целей", "Дальше"))
        list.add(BoardModel(R.drawable.board_third, "Развивайся", "Начинаем"))
        binding.viewPager.adapter = PagerAdapter(list, this)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().supportFragmentManager.beginTransaction().remove(OnBoardFragment())
            .commit()
    }

    override fun onClick() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("board_preference", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isShow", true).apply()
        findNavController().navigate(R.id.action_onBoardFragment_to_homeFragment)
    }

}