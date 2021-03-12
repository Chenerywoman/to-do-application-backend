package com.techreturners.model;

//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
// give human readable name to test
import org.junit.jupiter.api.DisplayName;

public class TaskTest {

    @Test
    @DisplayName("Test task description")
    public void testTaskDescription(){
        Task t = new Task("abc123", "Some task description", false);
        // expected, actual, message if it fails
         assertEquals("Some task description", t.getDescription(), "Task description was " +
                "incorrect");
    }

    @Test
    @DisplayName("Check completed is false by default")
    public void testDefaultCompletedStatus(){
        Task t = new Task("abc123", "Some Description");
        // expected, actual, message if it fails
        assertFalse(t.isCompleted(), "Task status was not false by default");
    }
}

