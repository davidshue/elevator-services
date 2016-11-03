package com.challenge;

import com.challenge.elevator.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.Timer;

/**
 * Created by david on 10/30/16.
 */
@Component
public class Bootstrapper {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private CallSystem callSystem;

    @Value("${elevator.services.stories}")
    private int stories;

    private Random random = new Random();

    @PostConstruct
    public void bootstrap() {
        randomUse();
    }

    @Scheduled(fixedDelay = 20000L) // fired every 20 seconds
    public void snapshot() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------ begin SNAPSHOT ------------\n");
        callSystem.getElevators().forEach(elevator -> sb.append("------ " + elevator + " ---------\n"));
        sb.append("------------ end SNAPSHOT ------------");
        logger.info(sb.toString());

        randomUse();
    }

    private void randomUse() {
        for(int i = 0; i < 25; i++) {
            CallRequestThread t1 = new CallRequestThread(callSystem, new CallRequest(Direction.up, random.nextInt(stories-1) + 1));
            CallRequestThread t2 = new CallRequestThread(callSystem, new CallRequest(Direction.down, random.nextInt(stories-1) + 2));
            t1.start();
            t2.start();
        }
    }
}
