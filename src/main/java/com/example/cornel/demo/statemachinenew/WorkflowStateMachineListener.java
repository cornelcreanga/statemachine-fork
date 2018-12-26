package com.example.cornel.demo.statemachinenew;

import static com.example.cornel.demo.statemachinenew.Events2.FAIL;
import static com.example.cornel.demo.statemachinenew.States2.*;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;


public class WorkflowStateMachineListener implements StateMachineListener<States2, Events2> {

    private StateMachine<States2, Events2> stateMachine;

    public WorkflowStateMachineListener(StateMachine<States2, Events2> stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void stateChanged(State<States2, Events2> from, State<States2, Events2> to) {
//        System.err.println("stateChanged "+from+" to "+to);
    }

    @Override
    public void stateEntered(State<States2, Events2> state) {
        System.err.println("stateEntered "+state);
    }

    @Override
    public void stateExited(State<States2, Events2> state) {
//        System.err.println("stateExited "+state);
    }

    @Override
    public void eventNotAccepted(Message<Events2> event) {
        System.out.println("eventNotAccepted "+event.toString());
    }

    @Override
    public void transition(Transition<States2, Events2> transition) {
        if (transition.getTarget().getId().equals(CLONE1_START)) {
            System.err.println("working "+CLONE1_START+" on "+Thread.currentThread().getName());
            sleep((long) (5000 + 3000 * Math.random()));
        }
        if (transition.getTarget().getId().equals(CLONE1_END)) {
            System.err.println("working "+CLONE1_END+" on "+Thread.currentThread().getName());
            sleep((long) (5000 + 3000 * Math.random()));
        }
        if (transition.getTarget().getId().equals(PIPELINE1_START)) {
            System.err.println("working "+PIPELINE1_START+" on "+Thread.currentThread().getName());
            sleep((long) (5000 + 3000 * Math.random()));
        }

        if (transition.getTarget().getId().equals(States2.CLONE2_START)) {
            System.err.println("working "+CLONE2_START+" on "+Thread.currentThread().getName());
            sleep((long) (5000 + 3000 * Math.random()));
        }

        //System.err.println("transition "+transition);
    }

    @Override
    public void transitionStarted(Transition<States2, Events2> transition) {
//        String source = transition.getSource()==null?"null":transition.getSource().getId().name();
//        String target = transition.getTarget()==null?"null":transition.getTarget().getId().name();
//        System.out.println("transitionStarted "+transition.getKind().name()+" source="+source+" target="+target);
    }

    @Override
    public void transitionEnded(Transition<States2, Events2> transition) {
//        if (transition.getTarget().getId().equals(CLONE1_START)) {
//            System.err.println("working "+CLONE1_START+" on "+Thread.currentThread().getName());
//            sleep((long) (5000 + 3000 * Math.random()));
//            stateMachine.sendEvent(FAIL);
//        }
//        if (transition.getTarget().getId().equals(CLONE1_END)) {
//            System.err.println("working "+CLONE1_END+" on "+Thread.currentThread().getName());
//            sleep((long) (5000 + 3000 * Math.random()));
//        }
//        if (transition.getTarget().getId().equals(PIPELINE1_START)) {
//            System.err.println("working "+PIPELINE1_START+" on "+Thread.currentThread().getName());
//            sleep((long) (5000 + 3000 * Math.random()));
//        }
//
//        if (transition.getTarget().getId().equals(States2.CLONE2_START)) {
//            System.err.println("working "+CLONE2_START+" on "+Thread.currentThread().getName());
//            sleep((long) (5000 + 3000 * Math.random()));
//        }
    }

    @Override
    public void stateMachineStarted(StateMachine<States2, Events2> stateMachine) {
//        System.err.println("stateMachineStarted "+stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<States2, Events2> stateMachine) {
//        System.err.println("stateMachineStopped "+stateMachine);
    }

    @Override
    public void stateMachineError(StateMachine<States2, Events2> stateMachine, Exception exception) {
//        System.err.println("stateMachineError "+stateMachine);
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {

    }

    @Override
    public void stateContext(StateContext<States2, Events2> stateContext) {

    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
