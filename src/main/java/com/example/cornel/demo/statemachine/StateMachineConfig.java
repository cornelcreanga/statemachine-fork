package com.example.cornel.demo.statemachine;


import static com.example.cornel.demo.statemachine.States.CHOICE;
import static com.example.cornel.demo.statemachine.States.CHOICE2;
import static com.example.cornel.demo.statemachine.States.CLONE1;
import static com.example.cornel.demo.statemachine.States.CLONE2;
import static com.example.cornel.demo.statemachine.States.END;
import static com.example.cornel.demo.statemachine.States.ERROR;
import static com.example.cornel.demo.statemachine.States.FORK;
import static com.example.cornel.demo.statemachine.States.JOIN;
import static com.example.cornel.demo.statemachine.States.NOT_STARTED;
import static com.example.cornel.demo.statemachine.States.PIPELINE1;
import static com.example.cornel.demo.statemachine.States.PIPELINE2;
import static com.example.cornel.demo.statemachine.States.SCORE;
import static com.example.cornel.demo.statemachine.States.TASKS;
import static com.example.cornel.demo.statemachine.States.TASKS1_DONE;
import static com.example.cornel.demo.statemachine.States.TASKS2_DONE;
import static com.example.cornel.demo.statemachine.States.VALIDATION1;
import static com.example.cornel.demo.statemachine.States.VALIDATION2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.StateMachineSystemConstants;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig
    extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Autowired
    private StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister;


    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
        throws Exception {

        states.withStates()
            .initial(NOT_STARTED)
            .fork(FORK)
            .state(TASKS)
            .join(JOIN)
            .choice(CHOICE)
            .state(ERROR)
            .and().withStates().parent(TASKS)
            .initial(CLONE1)
            .choice(CHOICE2)
            .state(PIPELINE1)
            .state(VALIDATION1)
            .end(TASKS1_DONE)
            .and().withStates().parent(TASKS)
            .initial(CLONE2)
            .state(PIPELINE2)
            .state(VALIDATION2)
            .end(TASKS2_DONE)
            .and().withStates().state(SCORE)
            .and().withStates().state(END)
            .and().withStates().state(ERROR)
            .end(END);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
        throws Exception {
        transitions
            .withExternal()
            .source(NOT_STARTED).target(FORK)
            .event(Events.RUN)
            .and()
            .withFork()
            .source(FORK).target(TASKS)
            .and().withExternal().source(CLONE1).target(CHOICE2).action(context -> {
                work(CLONE1);
                 //context.getExtendedState().getVariables().put("ERROR",true);
             })
            .and().withChoice().source(CHOICE2)
            .first(PIPELINE1, context -> !context.getExtendedState().getVariables().containsKey("ERROR"))
            .last(TASKS1_DONE)
            .and().withExternal().source(PIPELINE1).target(VALIDATION1).action(context -> work(PIPELINE1))
            .and().withExternal().source(VALIDATION1).target(TASKS1_DONE).action(context -> work(VALIDATION1))

            .and().withExternal().source(CLONE2).target(PIPELINE2).action(context -> work(CLONE2))
            .and().withExternal().source(PIPELINE2).target(VALIDATION2).action(context -> work(PIPELINE2))
            .and().withExternal().source(VALIDATION2).target(TASKS2_DONE).action(context -> work(VALIDATION2))
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
            .last(SCORE).
            and()
            .withExternal()
            .source(SCORE).target(END).action(context -> work(SCORE));

    }


    @Bean(name = StateMachineSystemConstants.TASK_EXECUTOR_BEAN_NAME)
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        return taskExecutor;
    }

    @Bean
    public StateMachineService<States, Events> stateMachineService(
        StateMachineFactory<States, Events> stateMachineFactory,
        StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
        throws Exception {
        config
            .withConfiguration()
            .and()
            .withPersistence()
            .runtimePersister(stateMachineRuntimePersister)
        ;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) { }
    }

    private static void work(States s) {
        System.err.println("working " + s + " on " + Thread.currentThread().getName());
        sleep((long) (2500 + 1500 * Math.random()));
    }

//    @Bean
//    public StateMachineService<States2, Events> stateMachineService(
//            StateMachineFactory<States2, Events> stateMachineFactory,
//            StateMachineRuntimePersister<States2, Events, String> stateMachineRuntimePersister) {
//        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
//    }

//end::snippetAE[]

}

