package com.example.smartslate.repository;

import com.example.smartslate.model.Subtask;

import java.util.List;

public interface ISubTask {

    public void createSubTask(Subtask subtask);
    public Subtask getSubtaskById(int subtaskID);
    public void updateSubtask(Subtask subtask);
    public void deleteSubtask(int subtaskId);
    public List<Subtask> getAllSubtasks();
}
