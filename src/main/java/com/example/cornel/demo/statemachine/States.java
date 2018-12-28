package com.example.cornel.demo.statemachine;

public enum States {

    NOT_STARTED,
    FORK, JOIN, CHOICE, CHOICE2,
    TASKS, CLONE1, PIPELINE1, VALIDATION1, TASKS1_DONE,
    CLONE2, PIPELINE2, VALIDATION2, TASKS2_DONE,
    ERROR,
    SCORE,
    END

}
