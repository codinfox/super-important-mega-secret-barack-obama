import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class Main {

	private Properties props;
	private StanfordCoreNLP pipeline;
	private Twitter t;
	private int positiveTweets, neutralTweets, negativeTweets;
	private List<Status> statuses;
	private HashMap<String, Integer> dailyScore = new HashMap<String, Integer>();
	
	
	public void start() throws TwitterException {
		System.out.println(dailyScore.get("AA"));
		props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
		t = (new TwitterFactory()).getInstance();
		t.getOAuth2Token();
		
		LinkedList<String> textss = new LinkedList<String>();
		Paging paging = new Paging(1, 1000);
		//String a = JOptionPane.showInputDialog("Input twitter name");
		statuses = t.getUserTimeline("adabroskii28", paging);
		User user = statuses.get(0).getUser();
		String URL = user.getBiggerProfileImageURL();
		for(Status s : statuses) {
			System.out.println(s.getText());
			textss.add(s.getText());
		}

		int[] scores = new int[textss.size()];
		
		
		
		calculateScores(scores, textss, statuses);		
		printResults(scores, textss);
		System.out.println(URL);
		System.out.println(dailyScore);
		
	}
	
	public void calculateScores(int[] scores, LinkedList<String> texts, List<Status> statuses) {
		for(int i = 0; i < texts.size(); i++) {
			Annotation annotation = pipeline.process(texts.get(i));
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
					System.out.println();
					if (dailyScore.get(statuses.get(i).getCreatedAt().toString().substring(0, 10)) != null)
						dailyScore.put(statuses.get(i).getCreatedAt().toString().substring(0, 10), dailyScore.get(statuses.get(i).getCreatedAt().toString().substring(0, 10)) + 1);
					else
						dailyScore.put(statuses.get(i).getCreatedAt().toString().substring(0, 10),  1);
				} else if(sentiment.equals("Negative")) {
					scores[i]--;
					if (dailyScore.get(statuses.get(i).getCreatedAt().toString().substring(0, 10)) != null)
						dailyScore.put(statuses.get(i).getCreatedAt().toString().substring(0, 10), dailyScore.get(statuses.get(i).getCreatedAt().toString().substring(0, 10)) - 1);
					else
						dailyScore.put(statuses.get(i).getCreatedAt().toString().substring(0, 10),  -1);
				}				
			}			
		}
	}
	
	public void printResults(int[] scores, LinkedList<String> texts) {
		for(int i = 0; i < texts.size(); i++) {
			if(scores[i] > 0) {
				System.out.println("Positive: " + texts.get(i));
				positiveTweets++;
			} else if(scores[i] < 0) {
				System.out.println("Negative: " + texts.get(i));
				negativeTweets++;
			} else { 
				System.out.println("Neutral: " + texts.get(i));
				neutralTweets++;
			}
			
		}

		System.out.println("Positive tweets: " + positiveTweets);
		System.out.println("Negative tweets: " + negativeTweets);
		System.out.println("Neutral tweets: " + neutralTweets);
	}

	public static void main(String[] args) throws TwitterException {
		new Main().start();
	}



}
