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
        // Arrange
        EmployeeTaskRepository repository = new EmployeeTaskRepository();
        int userId = 123; // Angiv den ønskede bruger-ID til testen

        // Act
        List<Task> tasks = repository.getEmployeeTasksByUserId(userId);

        // Assert
        Assertions.assertNotNull(tasks);
        // Tilføj yderligere asserteringer efter behov for at teste forventet opførsel
    }

    //Hvis testen lykkes, bekræfter det, at formatTotalTime()-metoden fungerer korrekt ved at
    // returnere den forventede formatterede tid for en given samlet tid. Hvis testen fejler,
    // indikerer det en afvigelse mellem den faktiske og forventede formattering,
    // og der kræves fejlfinding og korrektion af implementeringen.
    @Test
    void formatTotalTime() {
        // Arrange
        EmployeeTaskRepository repository = new EmployeeTaskRepository();
        int totalTime = 120; // Angiv den ønskede samlede tid i minutter til testen
        String expectedFormattedTime = "120:00"; // Opdateret forventning

        // Act
        String formattedTime = repository.formatTotalTime(BigDecimal.valueOf(totalTime));

        // Assert
        Assertions.assertEquals(expectedFormattedTime, formattedTime);
    }


   /* @Test
    void calculateTotalTimeSpent() {
        // Arrange
        EmployeeTaskRepository repository = new EmployeeTaskRepository();
        List<Task> tasks = new ArrayList<>(); // Opret en liste med opgaver til testen
        // Tilføj opgaver til listen efter behov
        Task task1 = new Task(1, "Task 1", "desc", "02:00", "status","projectname"); // Eksempel på en opgave med korrekt tidsformat
        Task task2 = new Task(2, "Task 2", "desc", "01:30", "status","projectname"); // Eksempel på en opgave med korrekt tidsformat
        tasks.add(task1);
        tasks.add(task2);

        int expectedTotalTime = 210; // Angiv det forventede samlede tidspunkt i minutter (2 timer + 1 time og 30 minutter = 210 minutter)

        // Act
        int totalTimeSpent = repository.calculateTotalTimeSpent(tasks);

        // Assert
        Assertions.assertEquals(expectedTotalTime, totalTimeSpent);
    }*/

}

