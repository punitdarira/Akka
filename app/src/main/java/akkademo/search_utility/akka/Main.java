package akkademo.search_utility.akka;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.AskPattern;
import akkademo.search_utility.akka.DirectorySearcherActor.CompleteCommand;
import akkademo.search_utility.akka.DirectorySearcherActor.SearchCommand;
import akkademo.search_utility.akka.DirectorySearcherActor.SearchCommandI;
import org.apache.commons.lang3.time.StopWatch;

public class Main {

	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ActorSystem<SearchCommandI> rootActor = ActorSystem.create(DirectorySearcherActor.create(), "root_actor");
		//AskPattern.as
		CompletionStage<SearchCommandI> finalResult = AskPattern.ask(rootActor, (me) -> new SearchCommand("C:\\", me),
				Duration.ofMinutes(5), rootActor.scheduler());
		finalResult.whenComplete((reply, failure) -> {
			stopWatch.stop();
			if (reply != null & reply instanceof CompleteCommand) {

				CompleteCommand completeCommand = (CompleteCommand)reply;
				System.out.println("No of files " + completeCommand.getNumberOfFiles());
				System.out.println("Time Took By Akka= " + stopWatch.getTime(TimeUnit.SECONDS));
				
			} else {
				System.out.println(failure);
			}
		});
	}

}
