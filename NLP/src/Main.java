import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class Main {


	public void start() {
		String text = "I am feeling very upset";
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		String[] texts = {
				"I am feeling very upset",
				"If you speed up when I turn on my blinker to get over, then there's a 100% chance you're an asshole",
				"I want Ben & Jerry's like now.",
				"I learned a little about myself on the way home from Michigan. What I learned is...  I LOVE FERGIE",
				"I went from 'don't fucking touch me' to a touchy feely person. What happened to me lmao",
				"Don't ask to hang with me if you are already chillin with 5 different chicks to start with. No thanks.",
				"Yeah I'm awake right now and yes I hate life",
				"good talk lmao",
				"Haha I feel like shit",
				"Okay so ashton can't cook. Still perfect."
		};
		
		for(String t : texts) {
			Annotation annotation = pipeline.process(t);
			List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				String sentiment = sentence.get(SentimentCoreAnnotations.ClassName.class);
				System.out.println(sentiment + ":\t" + sentence);
			}
		}
		
	}

	public static void main(String[] args) {
		new Main().start();
	}



}
