package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;
import models.*;

@With(Secure.class)
public class Twitter extends Controller {

	public static void index() {
		User user = Security.userConnected();
		List<Tweet> tweets = Tweet.find("select tweet from Tweet tweet, User user join user.follows user2 where user = ? and (tweet.author = user or tweet.author = user2) order by tweet.date desc", user).fetch();
		render(tweets);
	}

	public static void tweetsFrom(String username) {
		List<Tweet> tweets = Tweet.find("select tweet from Tweet tweet where tweet.author.username = ? order by tweet.date desc", username).fetch();
		render(username, tweets);
	}

	public static void allTweets() {
		List<Tweet> tweets = Tweet.find("order by date desc").fetch();
		render(tweets);
	}

	public static void follow(String username) {
		User user = controllers.Security.userConnected();
		User user2 = User.user(username);
		if (user2 != null && !user.follows(username)){
			user.follows.add(user2);
			user.save();
		}
		flash.success(play.i18n.Messages.get("successFollowing", username));
		index();
		//tweetsFrom(username);
	}
	
	public static void unfollow(String username) {
		User user = controllers.Security.userConnected();
		User user2 = User.user(username);
		if (user2 != null && user.follows(username)){
			user.follows.remove(user2);
			user.save();
		}
		flash.put("warning", play.i18n.Messages.get("warningUnfollowing", username));
		index();
		//tweetsFrom(username);
	}
	
	public static void stats() {
		List<Object[]> user_tweets = JPA.em().createQuery("select t.author.username, count(t) from Tweet t group by t.author").getResultList();
		render(user_tweets);
	}

	public static void create(String tweet) {
		checkAuthenticity();
		Tweet t = new Tweet(tweet, Security.userConnected());
		t.validateAndSave();

		if (validation.hasErrors()) {
			validation.keep();
		}

		index();
	}
	
}