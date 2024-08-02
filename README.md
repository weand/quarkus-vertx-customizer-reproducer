# Quarkus Dev Mode Hazelcast Hot Reload Issue

Run quarkus dev mode...

```shell script
./mvnw compile quarkus:dev
```

First iteration runs without issues, hazelcast cache processes custom object serialization...

```
2024-08-02 17:16:26,998 INFO  [com.git.wea.haz.HazelcastReproducer] (Quarkus Main Thread) #################### Successfully inserted ! task into map ####################
```

Pushing `s` for Restart results in serialization issues...

```
2024-08-02 17:20:05,417 ERROR [io.qua.dep.dev.IsolatedDevModeMain] (Aesh InputStream Reader) Failed to start quarkus: io.quarkus.dev.appstate.ApplicationStartException: java.lang.RuntimeException: Failed to start quarkus
        at io.quarkus.dev.appstate.ApplicationStateNotification.waitForApplicationStart(ApplicationStateNotification.java:63)
        at io.quarkus.runner.bootstrap.StartupActionImpl.runMainClass(StartupActionImpl.java:141)
        at io.quarkus.deployment.dev.IsolatedDevModeMain.restartApp(IsolatedDevModeMain.java:204)
        at io.quarkus.deployment.dev.IsolatedDevModeMain.restartCallback(IsolatedDevModeMain.java:185)
        at io.quarkus.deployment.dev.RuntimeUpdatesProcessor.doScan(RuntimeUpdatesProcessor.java:555)
        at io.quarkus.deployment.console.ConsoleStateManager.forceRestart(ConsoleStateManager.java:175)
        at io.quarkus.deployment.console.ConsoleStateManager.lambda$installBuiltins$0(ConsoleStateManager.java:112)
        at io.quarkus.deployment.console.ConsoleStateManager$1.accept(ConsoleStateManager.java:77)
        at io.quarkus.deployment.console.ConsoleStateManager$1.accept(ConsoleStateManager.java:49)
        at io.quarkus.deployment.console.AeshConsole.lambda$setup$1(AeshConsole.java:278)
        at org.aesh.terminal.EventDecoder.accept(EventDecoder.java:118)
        at org.aesh.terminal.EventDecoder.accept(EventDecoder.java:31)
        at org.aesh.terminal.io.Decoder.write(Decoder.java:133)
        at org.aesh.readline.tty.terminal.TerminalConnection.openBlocking(TerminalConnection.java:216)
        at org.aesh.readline.tty.terminal.TerminalConnection.openBlocking(TerminalConnection.java:203)
        at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
        at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
        at java.base/java.lang.Thread.run(Thread.java:1583)
Caused by: java.lang.RuntimeException: Failed to start quarkus
        at io.quarkus.runner.ApplicationImpl.doStart(Unknown Source)
        at io.quarkus.runtime.Application.start(Application.java:101)
        at io.quarkus.runtime.ApplicationLifecycleManager.run(ApplicationLifecycleManager.java:111)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:71)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:44)
        at io.quarkus.runtime.Quarkus.run(Quarkus.java:124)
        at io.quarkus.runner.GeneratedMain.main(Unknown Source)
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
        at io.quarkus.runner.bootstrap.StartupActionImpl$1.run(StartupActionImpl.java:115)
        ... 1 more
Caused by: com.hazelcast.nio.serialization.HazelcastSerializationException: Failed to serialize 'com.github.weand.hazelcastreproducer.task.Task'
        at com.hazelcast.internal.serialization.impl.SerializationUtil.handleSerializeException(SerializationUtil.java:129)
        at com.hazelcast.internal.serialization.impl.AbstractSerializationService.toBytes(AbstractSerializationService.java:241)
        at com.hazelcast.internal.serialization.impl.AbstractSerializationService.toBytes(AbstractSerializationService.java:217)
        at com.hazelcast.internal.serialization.impl.AbstractSerializationService.toData(AbstractSerializationService.java:202)
        at com.hazelcast.internal.serialization.impl.AbstractSerializationService.toData(AbstractSerializationService.java:157)
        at com.hazelcast.spi.impl.NodeEngineImpl.toData(NodeEngineImpl.java:419)
        at com.hazelcast.replicatedmap.impl.ReplicatedMapProxy.put(ReplicatedMapProxy.java:268)
        at com.github.weand.hazelcastreproducer.HazelcastReproducer.onStart(HazelcastReproducer.java:32)
        at com.github.weand.hazelcastreproducer.HazelcastReproducer_Observer_onStart_Ljc3CHlatA83Z5UhUpFV8l5xk1E.notify(Unknown Source)
        at io.quarkus.arc.impl.EventImpl$Notifier.notifyObservers(EventImpl.java:351)
        at io.quarkus.arc.impl.EventImpl$Notifier.notify(EventImpl.java:333)
        at io.quarkus.arc.impl.EventImpl.fire(EventImpl.java:80)
        at io.quarkus.arc.runtime.ArcRecorder.fireLifecycleEvent(ArcRecorder.java:156)
        at io.quarkus.arc.runtime.ArcRecorder.handleLifecycleEvents(ArcRecorder.java:107)
        at io.quarkus.deployment.steps.LifecycleEventsBuildStep$startupEvent1144526294.deploy_0(Unknown Source)
        at io.quarkus.deployment.steps.LifecycleEventsBuildStep$startupEvent1144526294.deploy(Unknown Source)
        ... 11 more
Caused by: com.hazelcast.nio.serialization.HazelcastSerializationException: The 'class java.util.UUID' cannot be serialized with zero configuration Compact serialization because this type is not supported yet. If you want to seri
alize 'class com.github.weand.hazelcastreproducer.task.Task' which uses this class in its fields, consider writing a CompactSerializer for either the 'class com.github.weand.hazelcastreproducer.task.Task' or the 'class java.util.
UUID'.
        at com.hazelcast.internal.serialization.impl.compact.CompactUtil.verifyFieldClassIsCompactSerializable(CompactUtil.java:194)
        at com.hazelcast.internal.serialization.impl.compact.zeroconfig.ValueReaderWriters.readerWriterFor(ValueReaderWriters.java:217)
        at com.hazelcast.internal.serialization.impl.compact.ReflectiveCompactSerializer.createFastReadWriteCaches(ReflectiveCompactSerializer.java:299)
        at com.hazelcast.internal.serialization.impl.compact.ReflectiveCompactSerializer.write(ReflectiveCompactSerializer.java:82)
        at com.hazelcast.internal.serialization.impl.compact.CompactStreamSerializer.buildSchema(CompactStreamSerializer.java:396)
        at com.hazelcast.internal.serialization.impl.compact.CompactStreamSerializer.writeObject(CompactStreamSerializer.java:147)
        at com.hazelcast.internal.serialization.impl.compact.CompactStreamSerializer.write(CompactStreamSerializer.java:116)
        at com.hazelcast.internal.serialization.impl.compact.CompactStreamSerializer.write(CompactStreamSerializer.java:109)
        at com.hazelcast.internal.serialization.impl.StreamSerializerAdapter.write(StreamSerializerAdapter.java:39)
        at com.hazelcast.internal.serialization.impl.AbstractSerializationService.toBytes(AbstractSerializationService.java:238)
        ... 25 more
```


