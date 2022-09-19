package com.siroca.todo09.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siroca.todo09.App
import com.siroca.todo09.ItemListener
import com.siroca.todo09.adapter.TaskAdapter
import com.siroca.todo09.databinding.FragmentHomeBinding
import com.siroca.todo09.room.TaskModel

class HomeFragment : Fragment(), ItemListener {
    lateinit var binding: FragmentHomeBinding
    var taskAdapter = TaskAdapter(arrayListOf(), this)
    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickers()

//        App.appDataBase.taskDao().getAll().observe(viewLifecycleOwner) { data ->
//            taskAdapter = TaskAdapter(data, this)
//            binding.recyclerView.adapter = taskAdapter
//        }

        db.collection("task").get().addOnSuccessListener { result ->
            val list = arrayListOf<TaskModel>()
            for (document in result) {
                val task = document.data["task"].toString()
                val date = document.data["date"].toString()
                val regular = document.data["regular"].toString()
                val model = TaskModel(task = task, date = date, regular = regular)
                list.add(model)
                Log.e("ololo", "onViewCreated:${document.id} => ${document.data}")
            }
            taskAdapter = TaskAdapter(list, this)
            binding.recyclerView.adapter = taskAdapter
        }
    }

    private fun doBounceAnimation(targetView: View) {
        val interpolator: Interpolator = object : Interpolator {
            override fun getInterpolation(v: Float): Float {
                return getPowOut(v, 2.0) //Add getPowOut(v,3); for more up animation
            }
        }
        val animator = ObjectAnimator.ofFloat(targetView, "translationY", 0f, 75f, 0f)
        animator.interpolator = interpolator
        animator.startDelay = 200
        animator.duration = 800
        animator.repeatCount = 5
        animator.start()
    }

    private fun getPowOut(elapsedTimeRate: Float, pow: Double): Float {
        return (1.toFloat() - Math.pow((1 - elapsedTimeRate).toDouble(), pow)).toFloat()
    }

    private fun initClickers() {
        with(binding) {
            fabAdd.setOnClickListener {
                doBounceAnimation(binding.recyclerView)
                val dialog = CreateTaskDialog()
                dialog.show(requireActivity().supportFragmentManager, "")
            }
        }
    }

    override fun itemUpdate(taskModel: TaskModel) {
        val dialog = CreateTaskDialog()
        val bundle = Bundle()
        bundle.putSerializable("model", taskModel)
        dialog.arguments = bundle
        dialog.show(requireActivity().supportFragmentManager, "")
    }
}