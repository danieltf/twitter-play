package controllers;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.libs.Crypto;
import play.libs.Mail;
import models.User;

public class Security extends Secure.Security {
	
	static boolean authenticate(String username, String password) {
		User user = User.find("byUsername", username).first();
		return user != null && Crypto.encryptAES(password).equals(user.password);
	}

	// public static boolean check(String profile) {
	// User user = userConnected();
	// if ("isAdmin".equals(profile)) {
	// return user.admin;
	// } else {
	// return false;
	// }
	// }

	public static User userConnected() {
		User user = User.find("byUsername", session.get("username")).first();
		return user;
	}

	public static void createAccount() {
		render();
	}

	public static void create(String username, String email, String password, String password2) {
		
		SimpleEmail e = new SimpleEmail();
		try {
			e.setFrom("pepe@adios.com");
			e.addTo("danieltf@gmail.com");
			e.setSubject("subject");
			e.setMsg("Message");
			Mail.send(e); 
		} catch (EmailException e1) {
			e1.printStackTrace();
		}
		
		
//		validation.required(username);
//		validation.required(email);
//		validation.required(password);
//		validation.required(password2);
//		if (User.user(username) != null)
//			validation.addError("usuario", "El nombre de usuario ya existe");
//		validation.email("email", email);
//		if (!password.equals(password2))
//			validation.addError("password2", "Las contraseñas no coinciden");
//		if (validation.hasErrors()){
//			flash.error("No se ha podido crear la cuenta de usuario. Revise los campos.");
//			validation.keep();
//			params.flash();
//			createAccount();
//		}
//		else{
//			User user = new User(username, Crypto.encryptAES(password), email);
//			user.save();
//			flash.success("Se ha creado correctamente la cuenta para el usuario " + username);
//			try {
//				Secure.login();
//			} catch (Throwable e) {}
//		}
	}

}