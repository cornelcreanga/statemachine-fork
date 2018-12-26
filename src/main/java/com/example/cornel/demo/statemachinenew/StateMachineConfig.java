package com.example.cornel.demo.statemachinenew;


import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.util.ObjectUtils;
import static com.example.cornel.demo.statemachinenew.States2.*;
import static com.example.cornel.demo.statemachinenew.Events2.*;

@Configuration
@EnableStateMachine
public class StateMachineConfig
        extends EnumStateMachineConfigurerAdapter<States2, Events2> {

    @Override
    public void configure(StateMachineStateConfigurer<States2, Events2> states)
            throws Exception {


        states.withStates()
            .initial(NOT_STARTED)
            .fork(FORK)
            .state(TASKS)
            .join(JOIN)
            .choice(CHOICE)
            .state(ERROR)
            .and().withStates().parent(TASKS)
                .initial(CLONE1_START)
                .state(CLONE1_END)
                .state(PIPELINE1_START)
                .end(PIPELINE1_END)
            .and().withStates().parent(TASKS)
                .initial(CLONE2_START)
                .end(CLONE2_END)
            .and().withStates().state(SCORE)
            .and().withStates().state(ERROR)
            .end(END);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States2, Events2> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(NOT_STARTED).target(FORK)
                .event(Events2.RUN)
                .and()
                .withFork()
                .source(FORK).target(TASKS)
                .and().withExternal().source(CLONE1_START).target(CLONE1_END)
                .and().withExternal().source(CLONE1_END).target(PIPELINE1_START)
                .and().withExternal().source(PIPELINE1_START).target(PIPELINE1_END)

                .and().withExternal().source(CLONE2_START).target(CLONE2_END)
                .and()
                .withJoin()
                .source(TASKS).target(JOIN)
                .and()
                .withExternal()
                .source(JOIN).target(CHOICE)
                .and()
                .withChoice()
                .source(CHOICE)
                .first(ERROR, stateContext -> false)
                .last(SCORE)
;
    }


    @Bean(name = StateMachineSystemConstants.TASK_EXECUTOR_BEAN_NAME)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        return taskExecutor;
    }

//    @Bean
//    public StateMachineService<States2, Events> stateMachineService(
//            StateMachineFactory<States2, Events> stateMachineFactory,
//            StateMachineRuntimePersister<States2, Events, String> stateMachineRuntimePersister) {
//        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
//    }

//end::snippetAE[]

}

