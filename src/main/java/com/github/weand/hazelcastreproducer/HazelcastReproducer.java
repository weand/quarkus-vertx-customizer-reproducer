package com.github.weand.hazelcastreproducer;

import java.time.Instant;
import java.util.UUID;

import com.github.weand.hazelcastreproducer.task.Task;
import com.github.weand.hazelcastreproducer.task.TaskState;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.replicatedmap.ReplicatedMap;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;

@Singleton
public class HazelcastReproducer {
    
    static final String CLUSTER_NAME = "test-cluster";
    
    static final String CACHE_NAME = "test-map";

    void onStart(@Observes final StartupEvent ev) {
        final HazelcastInstance hc = Hazelcast.getHazelcastInstanceByName(CLUSTER_NAME);
        final ReplicatedMap<UUID, Task> taskCacheMap = hc.getReplicatedMap(CACHE_NAME);
        final Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setCreatedAt(Instant.now());
        task.setState(TaskState.IN_PROGRESS);
        
        taskCacheMap.put(UUID.randomUUID(), task);

        Log.info("#################### Successfully inserted ! task into map ####################");
    }
}
