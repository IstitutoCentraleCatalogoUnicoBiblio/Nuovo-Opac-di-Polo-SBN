/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.almaviva.pssql.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import it.almaviva.opac.bean.amministrazione.LoginBean;

@Entity(name = "user_opac")
@Table(name = "user_opac")
public class User_opac {
	@Id
	@Column(name = "id_user")
	private Integer id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "userpwd")
	private String password;
	
	@Column(name = "superuser")
	private String superuser;
	
	@Column(name = "fl_canc")
	private String fl_canc;
	
	@Column(name = "cod_polo")
	private String cod_polo;

	
	public String getCod_polo() {
		return cod_polo;
	}

	public void setCod_polo(String cod_polo) {
		this.cod_polo = cod_polo.toUpperCase();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getSuperuser() {
		return superuser.equals("1");
	}

	public void setSuperuser(String superuser) {
		this.superuser = superuser;
	}

	public String getFl_canc() {
		return fl_canc;
	}

	public void setFl_canc(String fl_canc) {
		this.fl_canc = fl_canc;
	}

	public User_opac( String username, String password, String cod_polo) {
		super();
		this.username = username;
		this.password = password;
		this.cod_polo = cod_polo;
	}

	public User_opac() {
		super();
	}
	public static User_opac toUserModel(LoginBean usrLog, String codPolo){
		User_opac usr = new User_opac();
		usr.setUsername(usrLog.getUsername());
		usr.setPassword(usrLog.getPassword());
		usr.setCod_polo(codPolo);
		return usr;
	}
	
	
}
