package com.siroca.todo09

import com.siroca.todo09.room.TaskModel

interface ItemListener {
    fun itemUpdate(taskModel: TaskModel)
}