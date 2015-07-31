package de.accso.rs.example;

import java.util.Arrays;
import java.util.Date;

import scala.concurrent.Future;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class ReactiveStreamsAkka {

	public static void main(String[] args) {
		final Source<Integer, BoxedUnit> source = Source
				.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
 				.filter(i -> i % 2 == 0);
		Sink<Integer, Future<BoxedUnit>> sink = Sink.foreach(i -> format("Verarbeitet: %d", i));
		
		final RunnableGraph<BoxedUnit> runnable = source.to(sink);

		final ActorSystem system = ActorSystem.create("sys");
		final Materializer mat = ActorMaterializer.create(system);
		runnable.run(mat);
	}

	private static Integer format(String string, Integer f) {
		System.out.println(String.format("%tT [%s] " + string, new Date(), Thread.currentThread().getName(), f));
		return f;
	}
}
