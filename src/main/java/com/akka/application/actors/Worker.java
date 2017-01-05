package com.akka.application.actors;

import akka.actor.UntypedActor;
import com.akka.application.models.Result;
import com.akka.application.models.Work;

public class Worker extends UntypedActor {

    private double calculatePiFor(int start, int numberOfElements) {
        double acc = 0.0;
        for (int i = start * numberOfElements; i <= ((start + 1) * numberOfElements - 1); i++) {
            acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
        }
        return acc;
    }

    public void onReceive(Object message) {
        if (message instanceof Work) {
            Work work = (Work) message;
            double result = calculatePiFor(work.getStart(), work.getNumberOfElements());
            getSender().tell(new Result(result), getSelf());
        } else {
            unhandled(message);
        }
    }
}
