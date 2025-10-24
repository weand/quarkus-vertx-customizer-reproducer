/*
 * Copyright 2025 by AVM GmbH
 */

package com.github.weand.hazelcastreproducer;

import static com.github.weand.hazelcastreproducer.HazelcastReproducer.CACHE_NAME;
import static com.github.weand.hazelcastreproducer.HazelcastReproducer.CLUSTER_NAME;
import static io.vertx.spi.cluster.hazelcast.ConfigUtil.loadConfig;

import com.github.weand.hazelcastreproducer.task.Task;
import com.github.weand.hazelcastreproducer.task.TaskSerializer;
import com.hazelcast.config.Config;
import com.hazelcast.config.ReplicatedMapConfig;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.config.SerializerConfig;
import io.quarkus.vertx.VertxOptionsCustomizer;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Andreas Weise (a.weise@avm.de)
 */
@ApplicationScoped
public class MyCustomizer implements VertxOptionsCustomizer {

    @Override
    public void accept(VertxOptions options) {
        final Config hcConfig = loadConfig()
            .setClusterName(CLUSTER_NAME)
            .setInstanceName(CLUSTER_NAME);

        final SerializationConfig serialConf = hcConfig.getSerializationConfig();
        serialConf.addSerializerConfig(new SerializerConfig()
            .setImplementation(new TaskSerializer())
            .setTypeClass(Task.class));

        final ReplicatedMapConfig taskCacheConfig = new ReplicatedMapConfig(CACHE_NAME);
        taskCacheConfig.setAsyncFillup(false);
        hcConfig.addReplicatedMapConfig(taskCacheConfig);

        options.setClusterManager(new HazelcastClusterManager(hcConfig));
    }

}