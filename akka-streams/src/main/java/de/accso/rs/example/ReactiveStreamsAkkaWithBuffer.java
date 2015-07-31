package de.accso.rs.example;

import java.util.Arrays;
import java.util.Date;

import scala.concurrent.Future;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class ReactiveStreamsAkkaWithBuffer {

	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("stream");
		final Materializer mat = ActorMaterializer.create(system);

		final Source<Integer, BoxedUnit> source = Source.from(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		Sink<Integer, Future<BoxedUnit>> sink = Sink.foreach(i -> format("Verarbeitet: %d", i));

		RunnableGraph<BoxedUnit> runnable = source //
				.map(f -> format("Vor dem Buffer: %d", f))
				.buffer(1, OverflowStrategy.dropHead())
				.map(f -> format("Vor der Berechnung: %d", f))
				.map(f -> slowComputation(f))
				.to(sink);

		runnable.run(mat);
	}

	private static Integer format(String string, Integer f) {
		System.out.println(String.format("%tT [%s] " + string, new Date(), Thread.currentThread().getName(), f));
		return f;
	}

	private static Integer slowComputation(Integer f) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return f;
	}

}
