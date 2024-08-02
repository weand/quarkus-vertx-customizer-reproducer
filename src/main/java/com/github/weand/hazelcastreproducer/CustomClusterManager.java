package com.github.weand.hazelcastreproducer;

import static com.github.weand.hazelcastreproducer.HazelcastReproducer.CACHE_NAME;
import static com.github.weand.hazelcastreproducer.HazelcastReproducer.CLUSTER_NAME;

import com.github.weand.hazelcastreproducer.task.Task;
import com.github.weand.hazelcastreproducer.task.TaskSerializer;
import com.google.auto.service.AutoService;
import com.hazelcast.config.Config;
import com.hazelcast.config.ReplicatedMapConfig;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.config.SerializerConfig;

import io.vertx.core.spi.VertxServiceProvider;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;


@AutoService(VertxServiceProvider.class)
public class CustomClusterManager extends HazelcastClusterManager {

    public CustomClusterManager() {
        final Config hcConfig = loadConfig(); // read hazelcast-default.xml as base config
        hcConfig.setInstanceName(CLUSTER_NAME);
        
        final SerializationConfig serialConf = hcConfig.getSerializationConfig();
        serialConf.addSerializerConfig(new SerializerConfig()
            .setImplementation(new TaskSerializer())
            .setTypeClass(Task.class));
        
        final ReplicatedMapConfig taskCacheConfig = new ReplicatedMapConfig(CACHE_NAME);
        taskCacheConfig.setAsyncFillup(false);
        hcConfig.addReplicatedMapConfig(taskCacheConfig);
        
        setConfig(hcConfig);
    }

}
