package com.github.willferguson.nearcache.verticles;

import com.github.willferguson.nearcache.SharedDataKeys;
import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by will on 15/05/2016.
 */
public class SharedDataReaderVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SharedDataReaderVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        logger.info("Starting {}", this.getClass().getSimpleName());
        //Periodically read the value and print.
        vertx.periodicStream(1000)
                .toObservable()
                .flatMap(timer -> vertx.sharedData().getClusterWideMapObservable(SharedDataKeys.MAP_NAME.getCacheKey()))
                .flatMap(asyncMap -> asyncMap.getObservable(SharedDataKeys.MAP_KEY))
                .subscribe(
                         value -> {
                            logger.info("Getting value {} from shared map");
                        },
                        error -> logger.error("Error", error),
                        () -> logger.info("Done")
                );
        startFuture.complete();

    }
}
