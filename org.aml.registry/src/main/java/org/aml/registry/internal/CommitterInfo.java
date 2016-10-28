package org.aml.registry.internal;

public class CommitterInfo {
	public String login;
	public String password;
	public String email;
	public String name;

	public CommitterInfo(String login, String password, String email, String name) {
		this.login = login;
		this.password = password;
		this.email = email;
		this.name = name;
	}
}