import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class Main {

	private Properties props;
	private StanfordCoreNLP pipeline;

	public void start() {
		props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
		
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
		int[] scores = new int[texts.length];
		
		
		
		calculateScores(scores, texts);		
		printResults(scores, texts);
			
		
	}
	
	public void calculateScores(int[] scores, String[] texts) {
		for(int i = 0; i < texts.length; i++) {
			Annotation annotation = pipeline.process(texts[i]);
			List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				String sentiment = sentence.get(SentimentCoreAnnotations.ClassName.class);
				//System.out.println(sentiment + ":\t" + sentence);
				/*
				 * Can't just print the sentiment + sentence, since tweets are often longer than
				 * one sentence. So I calculate the scores of each sentence and the sum of 
				 * the sentence scores equals the tweet score.
				 */
				if(sentiment.equals("Positive")) {
					scores[i]++;
				} else if(sentiment.equals("Negative")) {
					scores[i]--;
				}				
			}			
		}
	}
	
	public void printResults(int[] scores, String[] texts) {
		for(int i = 0; i < texts.length; i++) {
			if(scores[i] > 0)
				System.out.println("Positive: " + texts[i]);
			else if(scores[i] < 0)
				System.out.println("Negative: " + texts[i]);
			else 
				System.out.println("Neutral: " + texts[i]);
		}
	}

	public static void main(String[] args) {
		new Main().start();
	}



}
