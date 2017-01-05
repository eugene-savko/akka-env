package com.akka.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import com.akka.application.actors.Listener;
import com.akka.application.actors.Master;
import com.akka.application.models.Calculate;

public class Pi {

    public static void main(String[] args) {
        Pi pi = new Pi();
        pi.calculate(4, 10000, 10000);
        System.out.println("Hello Akka!");
    }

    public void calculate(final int numberOfWorkers, final int numberOfElements, final int numberOfMessages) {
        // Create an Akka system
        ActorSystem system = ActorSystem.create("PiSystem");

        // create the result listener, which will print the result and shutdown the system
        final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");

        // create the master
        ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new Master(numberOfWorkers, numberOfMessages, numberOfElements, listener);
            }
        }), "master");

        // start the calculation
        master.tell(new Calculate());
    }
}
