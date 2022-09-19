package com.siroca.todo09.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert
    fun insert(taskModel: TaskModel)

    @Query("SELECT * FROM task_table")
    fun getAll(): LiveData<List<TaskModel>>

    @Update
    fun update(taskModel: TaskModel)

}