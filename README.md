Examples Reactive Streams
=========================

Examples for Akka Streams.

The project akka-streams contains examples in Akka Streams as one implementation of Reactive Streams. The examples are written in Java. 
The project also contains a maven pom. Each class contains a main method and can therefore be run directly.

## ReactiveStreamsAkka
This is a "Hello World" stream example (well, not hello world this time, but processing a very simple bounded stream). It shows the basic concepts of Akka Streams: Source, Sink and Materializer.

## ReactiveStreamsAkkaWithBuffer
This shows back pressure and buffers.

## ReactiveStreamsAkkaCustomSubscriber
This shows the interoperability of different Reactive Streams implementations. In the example, we use our own very simple demo implementation of a Subscriber. 
