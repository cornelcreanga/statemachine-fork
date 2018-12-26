package com.example.cornel.demo.statemachinenew;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.WithStateMachine;


@WithStateMachine
public class Workflow2 {


//    private StateMachine<States2, Events> stateMachine;
    @Autowired
    private StateMachine<States2, Events2> stateMachine;
//    @Autowired
//    StateMachineService<States2, Events> sms;

//    @Autowired
//    private StateMachineRuntimePersister<States2, Events, String> stateMachineRuntimePersister;

    public void init() throws InterruptedException {
        stateMachine.addStateListener(new WorkflowStateMachineListener(stateMachine));
        stateMachine.start();
        Thread.sleep(1000);
        //stateMachine = sms.acquireStateMachine("test");
        stateMachine.sendEvent(Events2.RUN);
        Thread.sleep(10*1000);
        System.out.println(stateMachine.getState());
//        stateMachine.sendEvent(Events2.RUN);
        Thread.sleep(2*1000);
        System.out.println(stateMachine.getState());
//        stateMachine.sendEvent(Events.RUN);

//        System.out.println("************************************************");
//        System.out.println(stateMachine1.getExtendedState().getVariables().get("test"));
//        System.out.println("************************************************");
//
//        stateMachine1.addStateListener(new WorkflowStateMachineListener(stateMachine1,stateMachineRuntimePersister) {
//        });
//
//        stateMachine1.getExtendedState().getVariables().put("test", "AAA"+System.currentTimeMillis());
//        stateMachine1.sendEvent(Events.ENV_RUNNING);
//
//
//        //stateMachineRuntimePersister.write(stateMachine1.,"11");
//        System.out.println(stateMachine1.getState().getId());
    }


//    public void init() {
//        stateMachine1 = sms.acquireStateMachine("11");
//
//        stateMachine2 = sms.acquireStateMachine("22");
//        stateMachine3 = sms.acquireStateMachine("33");
//
//        System.out.println("************************************************");
//        System.out.println(stateMachine1.getExtendedState().getVariables().get("test"));
//        System.out.println("************************************************");
//
//        stateMachine1.getExtendedState().getVariables().put("test", "VVVVV");
//        stateMachine1.sendEvent(Events.ENV_RUNNING);
//        System.out.println(stateMachine1.getState());
//        stateMachine3.sendEvent(Events.ENV_RUNNING);
//        stateMachine3.sendEvent(Events.ENV_CREATED);
//    }


//    @OnTransitionEnd(source = "CREATED", target = "ENV_RUNNING")
//    public void createdToRunning(ExtendedState extendedState) {
//        System.out.println("createdToRunning");
//        if (Math.random() > 0.5)
//            stateMachine1.sendEvent(Events.ENV_FAILED);
//        else {
//            stateMachine1.sendEvent(Events.ENV_CREATED);
//            stateMachine1.getExtendedState().getVariables().put("envRunning", "https://cq-dev.slack.com/messages/DALS61EF2/");
//        }
//    }
//
//    public void envCreatedToPipelineRunning(ExtendedState extendedState) {
//
//    }

}
