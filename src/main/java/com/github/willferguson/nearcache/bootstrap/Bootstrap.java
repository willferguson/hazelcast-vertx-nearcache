package com.github.willferguson.nearcache.bootstrap;

import com.github.willferguson.nearcache.verticles.LockTestVerticle;
import com.github.willferguson.nearcache.verticles.SharedDataReaderVerticle;
import com.github.willferguson.nearcache.verticles.SharedDataWriterVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by will on 15/05/2016.
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) throws Exception {
        CountDownLatch latch = new CountDownLatch(3);
        //Load up the default cluster manager configuration
        ClusterManager manager = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(manager);
        Vertx.clusteredVertx(options, result -> {
            Vertx vertx = result.result();
            vertx.deployVerticle(SharedDataWriterVerticle.class.getName(), res -> {
                if(!res.succeeded()) {
                    logger.error("Couldn't start DataWriter", res.cause());

                }
                latch.countDown();

            });
            vertx.deployVerticle(SharedDataReaderVerticle.class.getName(), res -> {
                if(!res.succeeded()) {
                    logger.error("Couldn't start DataReader", res.cause());
                }
                latch.countDown();
            });
            vertx.deployVerticle(LockTestVerticle.class.getName(), res -> {
                if(!res.succeeded()) {
                    logger.error("Couldn't start Lock test", res.cause());
                }
                latch.countDown();
            });
        });

        latch.await();



    }
}
