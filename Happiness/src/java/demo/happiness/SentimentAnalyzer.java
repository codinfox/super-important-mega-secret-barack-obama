package demo.happiness;
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
import twitter4j.conf.ConfigurationBuilder;

public class SentimentAnalyzer {

	private Properties props;
	private StanfordCoreNLP pipeline;
	private Twitter t;
	private int positiveTweets, neutralTweets, negativeTweets;
	private List<Status> statuses = new LinkedList<Status>();
	private HashMap<String, Integer> dailyScore = new HashMap<String, Integer>();
        private String userName = "";

	public void start() throws TwitterException {
		props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
		ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                  .setOAuthConsumerKey("vb8ds6NWWXqCUjFLhEMuOp0ms")
                  .setOAuthConsumerSecret("5UHP2K3R755CSz5xHjqECf7cmudyowjLavZt4dQyeT1mo8L14h")
                  .setOAuthAccessToken("2865116860-vOnLb2gFtDghuWU327ywdcLiqO1g22rMIBv71mX")
                  .setOAuthAccessTokenSecret("iF3vEef08UjThjRIKjXXtAT7j8Pn5l38a2AjgKAN3NliG");
                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter t = tf.getInstance();
                //t = (new TwitterFactory()).getInstance();
		
		LinkedList<String> textss = new LinkedList<String>();
                Paging paging;
                //for(int i = 1; i < 2000; i = i+200) {
                    
                    paging = new Paging(1, 200);
                    List<Status> newList = t.getUserTimeline(userName, paging);
                    statuses.addAll(newList);
                //}
		//String a = JOptionPane.showInputDialog("Input twitter name");
		
		
		for(Status s : statuses) {
			textss.add(s.getText());
		}

		int[] scores = new int[textss.size()];
		
		
		
		calculateScores(scores, textss, statuses);		
		printResults(scores, textss);
                System.out.println(dailyScore);
	}
	
	public void calculateScores(int[] scores, LinkedList<String> texts, List<Status> statuses) {
            Annotation annotation;
            List<CoreMap> sentences;
            String sentiment;
		for(int i = 0; i < texts.size(); i++) {
			annotation = pipeline.process(texts.get(i));
			sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {
				sentiment = sentence.get(SentimentCoreAnnotations.ClassName.class);
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
        
        public HashMap<String, Integer> getDailyScore() {
            return dailyScore;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getImageUrl() {
            User user = statuses.get(0).getUser();
            String URL = user.getBiggerProfileImageURL();
            return URL;
        }
}
