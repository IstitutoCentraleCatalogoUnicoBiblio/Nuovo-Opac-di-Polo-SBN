package it.almaviva.opac.mailer.props;

import org.apache.log4j.Logger;

import it.almaviva.opac.services.PropertiesLoader;
import it.almaviva.utils.opac.Util;

public class MailProperties {

	static Logger log = Logger.getLogger(MailProperties.class);

	private String dnshost;
	private String mittenteMail;
	private String username_login;
	private String psw_login;
	private String port_mail = "25";
	private Boolean isToLogin = false;
	private final String charset = "text/html; charset=\"utf-8\"";
	public final String mail_protocol = "smtp";


	public MailProperties() {
		super();
		loadProps();
	}

	private boolean parseBoolean(String value) {
		Boolean val = Boolean.valueOf(value);
		return val;
	}
	public void reloadProps() {
		log.info("Ricaricando le impostazioni della configurazione delle mail");
		loadProps();
	}

	private void loadProps() {
		log.info("lettura conf. Mail");

			String dns = PropertiesLoader.getProperty("DNS_MAIL");
			String mittente_mail = PropertiesLoader.getProperty("MITTENTE_MAIL");
			String mailIsToLogin = PropertiesLoader.getProperty("LOGIN_MAIL");
			String username_login = PropertiesLoader.getProperty("USERNAME_MAIL");
			String psw_login = PropertiesLoader.getProperty("PASSWORD_MAIL");
			String port_mail = PropertiesLoader.getProperty("PORT_MAIL");

			this.dnshost = (Util.isFilled(dns)) ? dns : "";
			this.mittenteMail = (Util.isFilled(mittente_mail)) ? mittente_mail : "";

			if (Util.isFilled(mailIsToLogin)) {
				log.info("Configurazione login mail server");
				this.username_login = (Util.isFilled(username_login)) ? username_login : "";
				this.psw_login = (Util.isFilled(psw_login)) ? psw_login : "";
				this.isToLogin = parseBoolean(mailIsToLogin);
				this.port_mail = (Util.isFilled(port_mail)) ? port_mail : "25";

			}
		}

	public String getDnshost() {
		return dnshost;
	}

	public void setDnshost(String dnshost) {
		this.dnshost = dnshost;
	}

	public String getMittenteMail() {
		return mittenteMail;
	}

	public void setMittenteMail(String mittenteMail) {
		this.mittenteMail = mittenteMail;
	}

	public String getUsername_login() {
		return username_login;
	}

	public void setUsername_login(String username_login) {
		this.username_login = username_login;
	}

	public String getPsw_login() {
		return psw_login;
	}

	public void setPsw_login(String psw_login) {
		this.psw_login = psw_login;
	}

	public Boolean getIsToLogin() {
		return isToLogin;
	}

	public void setIsToLogin(Boolean isToLogin) {
		this.isToLogin = isToLogin;
	}

	public String getCharset() {
		return charset;
	}

	
	
	public String getPort_mail() {
		return port_mail;
	}

	public void setPort_mail(String port_mail) {
		this.port_mail = port_mail;
	}

	@Override
	public String toString() {
		return "MailProperties [dnshost=" + dnshost + ", mittenteMail=" + mittenteMail + ", username_login="
				+ username_login + ", psw_login=" + psw_login + ", port_mail=" + port_mail + ", isToLogin=" + isToLogin
				+ ", charset=" + charset + "]";
	}

	
}
