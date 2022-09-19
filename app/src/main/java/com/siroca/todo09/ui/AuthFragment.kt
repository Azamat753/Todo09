package com.siroca.todo09.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.siroca.todo09.R
import com.siroca.todo09.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {
    private val binding = lazy { FragmentAuthBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.value.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()
    }

    private fun initClickers() {
        binding.value.signUp.setOnClickListener {
            findNavController().navigate(R.id.phoneNumberFragment)
        }
    }
}