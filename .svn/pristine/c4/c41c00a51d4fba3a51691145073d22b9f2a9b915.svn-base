package com.yks.bigdata.dto.system;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SystemUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer groupId;

    private String username;

    private String password;

    private String actStatus;

    private Date crtTime;
    
    private String salt;
    
    private List<String> role;//角色
    
    private List<String> competence;//权限

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getActStatus() {
        return actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus == null ? null : actStatus.trim();
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public List<String> getCompetence() {
		return competence;
	}

	public void setCompetence(List<String> competence) {
		this.competence = competence;
	}
	
}