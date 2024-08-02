package com.github.weand.hazelcastreproducer.task;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TaskSerializer implements StreamSerializer<Task> {

    @Override
    @Nonnull
    public Task read(@Nonnull ObjectDataInput in) throws IOException {
        // ORDER of reads and writes MUST be equal!

        Task t = new Task();

        t.setId(UUID.fromString(in.readString()));
        t.setState(TaskState.valueOf(in.readString()));
        t.setCreatedAt(Instant.parse(in.readString()));
        return t;
    }

    @Override
    public void write(@Nonnull ObjectDataOutput out, @Nonnull Task t) throws IOException {
        // ORDER of reads and writes MUST be equal!

        out.writeString(t.getId().toString());
        out.writeString(t.getState().name());
        out.writeString(DateTimeFormatter.ISO_INSTANT.format(t.getCreatedAt()));
    }

    @Override
    public int getTypeId() {
        return 42;
    }

}
