package com.example.smartslate.repository;

import com.example.smartslate.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class EmployeeTaskRepositoryTest {

    @Test
    void getEmployeeTasksByUserId() {

        EmployeeTaskRepository repository = new EmployeeTaskRepository();
        int userId = 123; // Angiv den ønskede bruger-ID til testen

        List<Task> tasks = repository.getEmployeeTasksByUserId(userId);

        Assertions.assertNotNull(tasks);
    }

    //Hvis testen lykkes, bekræfter det, at formatTotalTime()-metoden fungerer korrekt ved at
    // returnere den forventede formatterede tid for en given samlet tid. Hvis testen fejler,
    // indikerer det en afvigelse mellem den faktiske og forventede formattering,
    // og der kræves fejlfinding og korrektion af implementeringen.
    @Test
    void formatTotalTime() {
        EmployeeTaskRepository repository = new EmployeeTaskRepository();
        int totalTime = 120; // den ønskede samlede tid i minutter
        String expectedFormattedTime = "120:00";

        String formattedTime = repository.formatTotalTime(BigDecimal.valueOf(totalTime));

        Assertions.assertEquals(expectedFormattedTime, formattedTime);
    }


    @Test
    void calculateTotalTimeSpent() {
        EmployeeTaskRepository repository = new EmployeeTaskRepository();
        List<Task> tasks = new ArrayList<>();

        Task task1 = new Task(1, "Task 1", "desc"
                , BigDecimal.valueOf(120), "status", "projectname");
        Task task2 = new Task(2, "Task 2", "desc"
                , BigDecimal.valueOf(90), "status", "projectname");
        tasks.add(task1);
        tasks.add(task2);

        int expectedTotalTime = 210;

        int totalTimeSpent = repository.calculateTotalTimeSpent(tasks);

        Assertions.assertEquals(expectedTotalTime, totalTimeSpent);
    }


}

