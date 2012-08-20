package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import play.db.jpa.Model;
import play.mvc.Util;

@Entity
public class User extends Model {

	public String username;

	public String email;
	
	public String password;


	@ManyToMany
	public List<User> follows;

	public User(String username, String password, String email) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		follows = new ArrayList<User>();
	}
	
	public static User user(String username) {
		return User.find("select user from User user where User.username = ?", username).first();
	}

	public List<User> followers() {
		return User.find("select user from User user join user.follows user2 where user2.username = ?", username).fetch();
	}

	public boolean follows(String username) {
		for (User user : follows)
			if (user.username.equals(username))
				return true;
		return false;
	}
	
	public boolean isUserConnected() {
		return username.equals(controllers.Security.userConnected().username);
	}

}
