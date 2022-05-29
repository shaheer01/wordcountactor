package com.example.actors.wordcount.utils;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ActorProperties implements Serializable {

    private static final long serialVersionUID = -752980605549888478L;

    @Value("${actor.concurrency}")
    private int actorConcurrency;

}
