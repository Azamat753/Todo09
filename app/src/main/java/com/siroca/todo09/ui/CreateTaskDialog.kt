package com.siroca.todo09.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.siroca.todo09.App
import com.siroca.todo09.R
import com.siroca.todo09.room.TaskModel
import com.siroca.todo09.databinding.FragmentCreateTaskDialogBinding
import com.siroca.todo09.databinding.RegularDialogBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateTaskDialog : BottomSheetDialogFragment() {

    lateinit var binding: FragmentCreateTaskDialogBinding
    var cal = Calendar.getInstance()
    var taskModel: TaskModel? = null
    val db = Firebase.firestore
    private var storageReference: StorageReference? = null
    var filePath:Uri ?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateTaskDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun showRegularDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        val binding = RegularDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        with(binding) {

            everyDayRadioBtn.setOnClickListener {
                this@CreateTaskDialog.binding.regularBtn.text = everyDayRadioBtn.text.toString()
                dialog.dismiss()
            }
            everyWeekBtn.setOnClickListener {
                this@CreateTaskDialog.binding.regularBtn.text = everyWeekBtn.text.toString()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeBackground(view)
        initClicker()
        storageReference = FirebaseStorage.getInstance().reference


        if (arguments != null) {
            taskModel = arguments?.get("model") as TaskModel
            with(binding) {
                taskEd.setText(taskModel?.task)
                dateBtn.text = taskModel?.date
                regularBtn.text = taskModel?.regular
            }
        }
    }

    private fun updateDateInView() {
        val myFormat = "MM.dd.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.dateBtn.text = sdf.format(cal.time)
    }

    private fun initClicker() {
        with(binding) {
            dateBtn.setOnClickListener {
//                DatePickerDialog(
//                    requireContext(),
//                    dateSetListener,
//                    cal.get(Calendar.YEAR),
//                    cal.get(Calendar.MONTH),
//                    cal.get(Calendar.DAY_OF_MONTH)
//                ).show()
                uploadImage()
            }
            regularBtn.setOnClickListener {
                launchGallery()
            }
            applyBtn.setOnClickListener {

                if (taskEd.text.toString().isEmpty()) {
                    taskEd.error = "not task"
                } else {
                    val model = TaskModel(
                        task = taskEd.text.toString(),
                        date = dateBtn.text.toString(),
                        regular = regularBtn.text.toString()
                    )
                    if (arguments != null) {
                        val updateModel = TaskModel(
                            id = taskModel?.id,
                            task = taskEd.text.toString(),
                            date = dateBtn.text.toString(),
                            regular = regularBtn.text.toString()
                        )
                        App.appDataBase.taskDao().update(updateModel)
                    } else {
                        App.appDataBase.taskDao().insert(model)
                        db.collection("task").add(model).addOnSuccessListener {
                            Log.e("ololo", "pokemon: success ")
                        }.addOnFailureListener {
                            Log.e("ololo", "pokemon: ${it.message}")
                        }
                    }
                    findNavController().navigate(R.id.homeFragment)
                    dismiss()
                }
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                binding.imageVieww.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            uploadTask?.addOnSuccessListener {
                Toast.makeText(requireContext(), "Success upload", Toast.LENGTH_SHORT).show()
            }?.addOnFailureListener{
                Log.e("ololo", "uploadImage: ${it.message}", )
            }

        } else {
            Toast.makeText(requireContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeBackground(view: View) {
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
    }
}


