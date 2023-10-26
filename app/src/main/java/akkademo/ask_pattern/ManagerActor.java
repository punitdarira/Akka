package akkademo.ask_pattern;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;

public class ManagerActor extends AbstractBehavior<String> {

    private ManagerActor(ActorContext<String> context) {
        super(context);
    }

    public static Behavior<String> create(){
        return Behaviors.setup(ManagerActor::new);
    }
    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder().onMessageEquals("start", () -> {
            ActorRef<WorkerActor.Request> workerRef = getContext().spawn(WorkerActor.create(), "worker1");
            //worker1.tell("start");
            sendAskMessage(workerRef);
            sendAskMessage(workerRef);
            return this;
        }).onAnyMessage(message->{
            System.out.println("Recieved msg = " + message);
            return this;
        }).build();
    }

    private void sendAskMessage(ActorRef recipient) {
        getContext().ask(String.class, recipient, Duration.ofSeconds(2), me->{
            return new WorkerActor.Request("test", me);
        }, (res, throwable)->{
            if (res != null){
                return res;
            }
            else{
                return "didn't recieve anything";
            }
        });
    }
}
