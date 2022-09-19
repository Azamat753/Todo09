package com.siroca.todo09.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siroca.todo09.R
import com.siroca.todo09.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    lateinit var binding: FragmentWelcomeBinding
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    binding.welcomeTv.text = "Welcome\n${document.data["name"].toString()}"
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ololo", "Error getting documents.", exception)
            }
        binding.startBtn.setOnClickListener {
            findNavController().navigate(R.id.onBoardFragment)
        }
    }
}