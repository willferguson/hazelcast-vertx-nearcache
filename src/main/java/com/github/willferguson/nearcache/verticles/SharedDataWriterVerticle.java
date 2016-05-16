package com.github.willferguson.nearcache.verticles;

import com.github.willferguson.nearcache.SharedDataKeys;
import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by will on 15/05/2016.
 */
public class SharedDataWriterVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SharedDataWriterVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Random random = new Random();
        logger.info("Starting {}", this.getClass().getSimpleName());
        //Periodically read the value and print.
        vertx.periodicStream(10000)
                .toObservable()
                .flatMap(timer -> vertx.sharedData().getClusterWideMapObservable(SharedDataKeys.MAP_NAME.getCacheKey()))
                .flatMap(asyncMap -> {
                    int nextRandom = random.nextInt();
                    logger.info("Next random number is going to be {}", nextRandom);
                    return asyncMap.putObservable(SharedDataKeys.MAP_KEY, nextRandom);
                })
                .subscribe(
                        value -> {

                        },
                        error -> logger.error("Error", error),
                        () -> logger.info("Added new random number to the map")
                );
        startFuture.complete();

    }
}

