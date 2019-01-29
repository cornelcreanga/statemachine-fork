package com.example.cornel.demo.statemachine;


import static com.example.cornel.demo.statemachine.States.CHOICE;
import static com.example.cornel.demo.statemachine.States.CHOICE_PIPELINE1_CREATE;
import static com.example.cornel.demo.statemachine.States.CHOICE_PIPELINE1_RUN;
import static com.example.cornel.demo.statemachine.States.CHOICE_PIPELINE2_CREATE;
import static com.example.cornel.demo.statemachine.States.CHOICE_PIPELINE2_RUN;
import static com.example.cornel.demo.statemachine.States.CLONE1;
import static com.example.cornel.demo.statemachine.States.CLONE2;
import static com.example.cornel.demo.statemachine.States.END;
import static com.example.cornel.demo.statemachine.States.ERROR;
import static com.example.cornel.demo.statemachine.States.FORK;
import static com.example.cornel.demo.statemachine.States.JOIN;
import static com.example.cornel.demo.statemachine.States.NOT_STARTED;
import static com.example.cornel.demo.statemachine.States.PIPELINE1_CREATE;
import static com.example.cornel.demo.statemachine.States.PIPELINE1_METRICS;
import static com.example.cornel.demo.statemachine.States.PIPELINE1_RUN;
import static com.example.cornel.demo.statemachine.States.PIPELINE2_CREATE;
import static com.example.cornel.demo.statemachine.States.PIPELINE2_METRICS;
import static com.example.cornel.demo.statemachine.States.PIPELINE2_RUN;
import static com.example.cornel.demo.statemachine.States.SCORE;
import static com.example.cornel.demo.statemachine.States.TASKS;
import static com.example.cornel.demo.statemachine.States.TASKS1_DONE;
import static com.example.cornel.demo.statemachine.States.TASKS2_DONE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.statemachine.StateContext;
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
            .choice(CHOICE_PIPELINE1_CREATE)
            .state(PIPELINE1_CREATE)
            .choice(CHOICE_PIPELINE1_RUN)
            .state(PIPELINE1_RUN)
            .state(PIPELINE1_METRICS)
            .end(TASKS1_DONE)
            .and().withStates().parent(TASKS)
            .initial(CLONE2)
            .choice(CHOICE_PIPELINE2_CREATE)
            .state(PIPELINE2_CREATE)
            .choice(CHOICE_PIPELINE2_RUN)
            .state(PIPELINE2_RUN)
            .state(PIPELINE2_METRICS)
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
            .and().withExternal().source(CLONE1).target(CHOICE_PIPELINE1_CREATE).action(context -> {
            Util.work(CLONE1);
//                context.getExtendedState().getVariables().put("ERROR",true);
        })
            .and().withChoice().source(CHOICE_PIPELINE1_CREATE)
            .first(PIPELINE1_CREATE, context -> !isError(context, "ERROR1"))
            .last(TASKS1_DONE)

            .and().withExternal().source(PIPELINE1_CREATE).target(CHOICE_PIPELINE1_RUN).action(context -> {
            Util.work(PIPELINE1_CREATE);
            //context.getExtendedState().getVariables().put("ERROR1",true);
        })
            .and().withChoice().source(CHOICE_PIPELINE1_RUN)
            .first(PIPELINE1_RUN, context -> !isError(context, "ERROR1"))
            .last(TASKS1_DONE)

            .and().withExternal().source(PIPELINE1_RUN).target(PIPELINE1_METRICS).action(context -> Util.work(PIPELINE1_RUN))
            .and().withExternal().source(PIPELINE1_METRICS).target(TASKS1_DONE).action(context -> Util.work(PIPELINE1_METRICS))

            .and().withExternal().source(CLONE2).target(CHOICE_PIPELINE2_CREATE).action(context -> {
            Util.work(CLONE2);
            context.getExtendedState().getVariables().put("ERROR2", true);
        })
            .and().withChoice().source(CHOICE_PIPELINE2_CREATE)
            .first(PIPELINE2_CREATE, context -> !isError(context, "ERROR2"))
            .last(TASKS2_DONE)

            .and().withExternal().source(PIPELINE2_CREATE).target(CHOICE_PIPELINE2_RUN).action(context -> {
            Util.work(PIPELINE2_CREATE);
            context.getExtendedState().getVariables().put("ERROR2", true);
        })
            .and().withChoice().source(CHOICE_PIPELINE2_RUN)
            .first(PIPELINE2_RUN, context -> !isError(context, "ERROR"))
            .last(TASKS2_DONE)

            .and().withExternal().source(PIPELINE2_RUN).target(PIPELINE2_METRICS).action(context -> Util.work(PIPELINE2_RUN))
            .and().withExternal().source(PIPELINE2_METRICS).target(TASKS2_DONE).action(context -> Util.work(PIPELINE2_METRICS))

//            .and().withExternal().source(CLONE2).target(PIPELINE2_CREATE).action(context -> {
//                Util.work(CLONE2);
//            })
//            .and().withExternal().source(PIPELINE2_CREATE).target(PIPELINE2_RUN).action(context -> Util.work(PIPELINE2_CREATE))
//            .and().withExternal().source(PIPELINE2_RUN).target(PIPELINE2_METRICS).action(context -> Util.work(PIPELINE2_RUN))
//            .and().withExternal().source(PIPELINE2_METRICS).target(TASKS2_DONE).action(context -> Util.work(PIPELINE2_METRICS))

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
            .source(SCORE).target(END).action(context -> Util.work(SCORE));

    }

    private boolean isError(StateContext<States, Events> context, String error) {
        return context.getExtendedState().getVariables().containsKey(error);
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
//            .and()
//            .withPersistence()
//            .runtimePersister(stateMachineRuntimePersister)
        ;
    }

}

