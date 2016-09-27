package com.github.willferguson.nearcache.verticles;

import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by will on 25/05/16.
 */
public class LockTestVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(LockTestVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.sharedData().getLockWithTimeoutObservable("key_lock", 1000L)
                .subscribe(
                        lock -> {
                            logger.info("Obtained lock", lock);
                            logger.info("Releasing lock");
                            lock.release();
                        },
                        error -> {
                            error.printStackTrace();
                            startFuture.fail(error);
                        },
                        () -> {
                            logger.info("Done");
                            startFuture.complete();
                        }
                );
    }
}
