package com.github.weand.hazelcastreproducer.task;

public enum TaskState {
    IN_PROGRESS,
    DONE,
    CANCELED,
    FAILURE,
    EXPIRED
}
