package akkademo.ask_pattern;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class WorkerActor extends AbstractBehavior<WorkerActor.Request> {

    public static class Request {
        public final String query;
        public final ActorRef<String> replyTo;

        public Request(String query, ActorRef<String> replyTo) {
            this.query = query;
            this.replyTo = replyTo;
        }
    }
    int i = 0;
    private WorkerActor(ActorContext<Request> context) {
        super(context);
    }

    public static Behavior<Request> create(){
        return Behaviors.setup(WorkerActor::new);
    }
    @Override
    public Receive<Request> createReceive() {
        return newReceiveBuilder().onAnyMessage( (message) -> {
            if(i != 0){
                Thread.sleep(3000);
            }
            i++;
            message.replyTo.tell(Math.random()+"");
            return this;
        }).build();
    }
}
