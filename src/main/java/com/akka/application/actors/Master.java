package com.akka.application.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import com.akka.application.models.Calculate;
import com.akka.application.models.PiApproximation;
import com.akka.application.models.Result;
import com.akka.application.models.Work;

import java.util.concurrent.TimeUnit;

public class Master extends UntypedActor {
    private final int numberOfMessages;
    private final int numberOfElements;

    private double pi;
    private int numberOfResults;
    private final long start = System.currentTimeMillis();

    private final ActorRef listener;
    private final ActorRef workerRouter;

    public Master(final int numberOfWorkers, int numberOfMessages, int numberOfElements, ActorRef listener) {
        this.numberOfMessages = numberOfMessages;
        this.numberOfElements = numberOfElements;
        this.listener = listener;
        this.workerRouter = this.getContext().actorOf(new Props(Worker.class).withRouter(new RoundRobinRouter(numberOfWorkers)), "workerRouter");
    }

    public void onReceive(Object message) {
        if (message instanceof Calculate) {
            for (int start = 0; start < numberOfMessages; start++) {
                workerRouter.tell(new Work(start, numberOfElements), getSelf());
            }
        } else if (message instanceof Result) {
            Result result = (Result) message;
            pi += result.getValue();
            numberOfResults += 1;
            if (numberOfResults == numberOfMessages) {
                // Send the result to the listener
                Duration duration = Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
                listener.tell(new PiApproximation(pi, duration), getSelf());
                // Stops this actor and all its supervised children
                getContext().stop(getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
