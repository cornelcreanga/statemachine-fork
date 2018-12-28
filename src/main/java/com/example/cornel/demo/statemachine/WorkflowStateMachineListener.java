package com.example.cornel.demo.statemachine;


import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;


public class WorkflowStateMachineListener implements StateMachineListener<States, Events> {

    private StateMachine<States, Events> stateMachine;

    public WorkflowStateMachineListener(StateMachine<States, Events> stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void stateChanged(State<States, Events> from, State<States, Events> to) {
//        System.err.println("stateChanged "+from+" to "+to);
    }

    @Override
    public void stateEntered(State<States, Events> state) {
//        System.out.println("stateEntered "+state.getId());
    }

    @Override
    public void stateExited(State<States, Events> state) {
//        System.err.println("stateExited "+state);
    }

    @Override
    public void eventNotAccepted(Message<Events> event) {
        //System.out.println("eventNotAccepted "+event.toString());
    }

    @Override
    public void transition(Transition<States, Events> transition) {
    }

    @Override
    public void transitionStarted(Transition<States, Events> transition) {
    }

    @Override
    public void transitionEnded(Transition<States, Events> transition) {
    }

    @Override
    public void stateMachineStarted(StateMachine<States, Events> stateMachine) {
//        System.err.println("stateMachineStarted "+stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<States, Events> stateMachine) {
//        System.err.println("stateMachineStopped "+stateMachine);
    }

    @Override
    public void stateMachineError(StateMachine<States, Events> stateMachine, Exception exception) {
//        System.err.println("stateMachineError "+stateMachine);
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {

    }

    @Override
    public void stateContext(StateContext<States, Events> stateContext) {

    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
