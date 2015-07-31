package de.accso.rs.example;

import java.util.Arrays;
import java.util.Date;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class ReactiveStreamsAkkaCustomSubscriber {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("streams");
		final Materializer mat = ActorMaterializer.create(system);

		final Source<Integer, BoxedUnit> source = Source.from(
				Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).filter(i -> i % 2 == 0);

		Sink<Integer, BoxedUnit> sink = Sink.create(new Subscriber<Integer>() {
			private Subscription sub;
			private static final int MAX_RECEIVED = 3;
			private int received = 0;

			public void onSubscribe(Subscription sub) {
				this.sub = sub;
				sub.request(1);
			}

			public void onNext(Integer i) {
				format("Verarbeitet: %d", i);
				received++;
				if (received >= MAX_RECEIVED) {
					sub.cancel();
				} else {
					sub.request(1);
				}
			}

			public void onError(Throwable t) {
				// empty
			}

			public void onComplete() {
				// empty
			}
		});

		final RunnableGraph<BoxedUnit> runnable = source.to(sink);

		runnable.run(mat);
	}

	private static Integer format(String string, Integer f) {
		System.out.println(String.format("%tT [%s] " + string, new Date(), Thread
				.currentThread().getName(), f));
		return f;
	}
}
