package controllers;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.libs.Crypto;
import play.libs.Mail;
import models.User;

public class Security extends Secure.Security {
	
	static boolean authenticate(String username, String password) {
		User user = User.find("byUsername", username).first();
		if (user == null || !Crypto.encryptAES(password).equals(user.password)){
			flash.put("open", "login");
			return false;
		}
		return true;
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
		render("Secure/login.html");
	}
	
	
	public static void login2(){
		render();
	}
	
//	SimpleEmail e = new SimpleEmail();
//	try {
//		e.setFrom("pepe@adios.com");
//		e.addTo("danieltf@gmail.com");
//		e.setSubject("subject");
//		e.setMsg("Message");
//		Mail.send(e); 
//	} catch (EmailException e1) {
//		e1.printStackTrace();
//	}

	
	// VOLVER A PONER LA VALIDACION DEL EMAIL
	
	public static void create(String username2, String email, String password2, String password3) {
		validation.required(username2);
		validation.required(email);
		validation.required(password2);
		validation.required(password3);
		if (User.user(username2) != null)
			validation.addError("username2", "Usuario existente");
//		validation.email("email", email).message("Formato incorrecto");
		if (!password2.equals(password3))
			validation.addError("password3", "Contraseña distinta");
		if (validation.hasErrors()){
			flash.error("No se ha podido crear la cuenta de usuario. Revise los campos.");
			flash.put("open", "create");
			validation.keep();
			params.flash();
		}
		else{
			User user = new User(username2, Crypto.encryptAES(password2), email);
			user.save();
			flash.success("Se ha creado correctamente el usuario " + username2 + ". Haz login  con tu nueva cuenta.");
			flash.put("open", "login");
		}
		try {
			Secure.login();
		} catch (Throwable e) {}
	}

}