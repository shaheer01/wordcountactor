package com.example.actors.wordcount.actorsystem;

import com.example.actors.wordcount.actors.CustomActor;

import com.example.actors.wordcount.utils.ActorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ActorWordCount {

    private static List<CustomActor<String>> layer2Actor;
    private static List<CustomActor<String>> layer3Actor;
    private static final ConcurrentHashMap<String, Integer> finalOutput = new ConcurrentHashMap<String, Integer>();
    @Autowired
    ActorProperties actorProperties;

    public void wordCount (String sentences){
        layer2Actor = new ArrayList<>(10);
        layer3Actor = new ArrayList<>(10);
        int c;
        for (c = 0; c < 10; c++) {
            CustomActor<String> layer3ActorN = ActorSystem.create((actor, message) -> {
                        log.info("Processing: " + message);
                        //:TODO: Optimization around this.
                        if (finalOutput.containsKey(message)) {
                            finalOutput.put(message, finalOutput.get(message) + 1);
                        } else {
                            finalOutput.put(message, 1);
                        }

                        finalOutput.forEach((key, value) -> log.info(key + " " + value));
                        log.info(String.valueOf("Final wordcount" + finalOutput));
                    }, (actor, exception) -> log.error(String.valueOf(exception))
            );
            layer3Actor.add(layer3ActorN);
        }

        for (c = 0; c < 10; c++) {
            CustomActor<String> layer2ActorN = ActorSystem.create((actor, message) -> {
                        List<String> list =
                                Stream.of(message).map(k -> k.split("\\W+")).flatMap(Arrays::stream).collect(Collectors.toList());
                        log.info("Output layer2Actor:" + list);

                        list.forEach(word -> {
                            int choose = Math.abs(word.toLowerCase().hashCode() % actorProperties.getActorConcurrency());
                            log.info("Word: " + word + " Choose: " + choose);
                            layer3Actor.get(choose).send(word.toLowerCase());
                        });

                    }, (actor, exception) -> log.error(String.valueOf(exception))
            );
            layer2Actor.add(layer2ActorN);
        }

        //Spawning multiple actors for layer2Actor, layer1Actor.
        CustomActor<String> layer1Actor = ActorSystem.create((actor, message) -> {
                    List<String> list = Stream.of(message).map(k -> k.split("\\.")).flatMap(Arrays::stream).collect(Collectors.toList());
                    log.info("Output layer1Actor:" + list);
                    //Spawning multiple actors for layer2Actor, layer1Actor.
                    for (int j = 0; j < list.size(); j++) {
                        log.info("Sentence: " + list.get(j));
                        layer2Actor.get(j % 10).send(list.get(j));
                    }
                }, (actor, exception) -> log.error(String.valueOf(exception))
        );
        log.info("Printing layer1Actor:" + layer1Actor);

        layer1Actor.send(sentences);

        ActorSystem.shutdown();

    }
    public ConcurrentHashMap<String, Integer> getCount(){
        return finalOutput;
    }
}
