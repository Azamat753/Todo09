package com.siroca.todo09.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siroca.todo09.R
import com.siroca.todo09.databinding.FragmentCreateProfileBinding
import com.siroca.todo09.showToast
import kotlinx.coroutines.flow.combine

class CreateProfileFragment : Fragment() {

    lateinit var binding: FragmentCreateProfileBinding
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicker()
    }

    private fun initClicker() {
        with(binding) {
            okBtn.setOnClickListener {
                val user = hashMapOf(
                    "name" to nameEd.text.toString(),
                    "password" to passwordEd.text.toString().trim()
                )
                db.collection("user").add(user).addOnSuccessListener {
                    requireContext().showToast("Успех")
                    findNavController().navigate(R.id.welcomeFragment)
                }.addOnFailureListener {
                    requireContext().showToast("Облом")
                }
            }
        }
    }
}