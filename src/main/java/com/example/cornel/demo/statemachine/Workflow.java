package com.example.cornel.demo.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.service.StateMachineService;


@WithStateMachine
public class Workflow {

    private StateMachine<States, Events> stateMachine;
    @Autowired
    StateMachineService<States, Events> sms;

    public void init() throws InterruptedException {

        stateMachine = sms.acquireStateMachine("test");
        stateMachine.addStateListener(new WorkflowStateMachineListener(stateMachine));
        stateMachine.start();
        Thread.sleep(1000);
        //stateMachine = sms.acquireStateMachine("test");
        stateMachine.sendEvent(Events.RUN);
        Thread.sleep(15 * 1000);
        System.out.println(stateMachine.getState().getId());

    }
}
