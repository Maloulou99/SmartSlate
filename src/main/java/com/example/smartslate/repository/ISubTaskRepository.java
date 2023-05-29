package com.example.smartslate.repository;

import com.example.smartslate.model.Subtask;

import java.util.List;

public interface ISubTaskRepository {

     void createSubTask(Subtask subtask);
     Subtask getSubtaskById(int subtaskID);
     void updateSubtask(Subtask subtask);
     void deleteSubtask(int subtaskId);
     List<Subtask> getAllSubtasks();
}
