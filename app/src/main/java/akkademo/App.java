/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package akkademo;

import akka.actor.typed.ActorSystem;

public class App {

    public static void main(String[] args) {
    	ActorSystem<String> actorSystem = ActorSystem.create(FirstBehavior.create(), "FirstActor");
    	/*actorSystem.tell("DemoMsg");
    	actorSystem.tell("Path");
    	actorSystem.tell("ChildActor");
    	actorSystem.tell("Anothermsg");
    	*/
    	for(int i = 0 ; i<20 ; i++) {
    		actorSystem.tell("ChildActorAndCalcPrime");
    	}
    }
}
