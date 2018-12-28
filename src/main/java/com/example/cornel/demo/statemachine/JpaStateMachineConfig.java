package com.example.cornel.demo.statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
public class JpaStateMachineConfig {

    @Bean
    public StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister(
        JpaStateMachineRepository jpaStateMachineRepository) {

        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

}
