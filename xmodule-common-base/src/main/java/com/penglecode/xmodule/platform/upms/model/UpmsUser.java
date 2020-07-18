package com.penglecode.xmodule.platform.upms.model;

import java.util.LinkedHashMap;
import java.util.Map;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.platform.upms.consts.em.UpmsUserStatusEnum;
import com.penglecode.xmodule.platform.upms.consts.em.UpmsUserTypeEnum;

/**
 * UPMS用户 (upms_user) 实体类
 * 
 * @author Mybatis-Generator
 * @date	2019年12月24日 下午 17:12:11
 */
@Model(name="UPMS用户", alias="User")
public class UpmsUser implements BaseModel<UpmsUser> {
     
    private static final long serialVersionUID = 1L;

    /** 用户id */
    @Id
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 真实姓名 */
    private String realName;

    /** 密码 */
    private String password;

    /** 用户类型：0-系统用户,1-普通用户 */
    private Integer userType;

    /** 昵称 */
    private String nickName;

    /** 手机号码 */
    private String mobilePhone;

    /** 电子邮箱 */
    private String email;

    /** 用户头像 */
    private String userIcon;

    /** 所属组织机构ID：null-未设置，0-所有组织，其他值则为具体的组织机构ID */
    private Long orgId;
    
    /** 用户状态:0-冻结,1-正常 */
    private Integer status;

    /** 最后登录时间 */
    private String lastLoginTime;

    /** 登录次数 */
    private Integer loginTimes;
    
    /** 创建时间 */
    private String createTime;

    /** 创建者,用户表的id */
    private Long createBy;

    /** 更新时间 */
    private String updateTime;

    /** 更新者,用户表的id */
    private Long updateBy;
    
    //以下属于辅助字段
    
    private String repassword;
    
    private String oldpassword;
    
    private String statusName;
    
    private String userTypeName;
    
    private String userIconUrl;
    
    /** 用户在线状态 */
    private Boolean online;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getUserIconUrl() {
		return userIconUrl;
	}

	public void setUserIconUrl(String userIconUrl) {
		this.userIconUrl = userIconUrl;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

	@Override
	public UpmsUser decode() {
		if (status != null) {
            UpmsUserStatusEnum em = UpmsUserStatusEnum.getStatus(status);
            if (em != null) {
                setStatusName(em.getStatusName());
            }
        }
        if (userType != null) {
            UpmsUserTypeEnum em = UpmsUserTypeEnum.getUserType(userType);
            if (em != null) {
                setUserTypeName(em.getTypeName());
            }
        }
        if (userIcon != null && !"".equals(userIcon.trim())) {
        	String userIconUrl = userIcon;
        	if(userIconUrl.toLowerCase().startsWith("http")){ //默认的头像或者以http开头的全url路径头像
        		//nothing to do
        	} else { //默认以文件服务器(Nginx/Apache)映射的静态文件来处理
        		userIconUrl = GlobalConstants.GLOBAL_APP_CONFIG.getFileServerUrl() + userIconUrl;
        	}
        	setUserIconUrl(userIconUrl);
        }
		return this;
	}
	
	public MapBuilder mapBuilder() {
        return new MapBuilder();
    }

    /**
     * Auto generated by Mybatis Generator
     */
    public class MapBuilder {
         
        private final Map<String, Object> modelProperties = new LinkedHashMap<String,Object>();

        MapBuilder() {
            
        }

        public MapBuilder withUserId(Long ... userId) {
            modelProperties.put("userId", BaseModel.first(userId, getUserId()));
            return this;
        }

        public MapBuilder withUserName(String ... userName) {
            modelProperties.put("userName", BaseModel.first(userName, getUserName()));
            return this;
        }

        public MapBuilder withRealName(String ... realName) {
            modelProperties.put("realName", BaseModel.first(realName, getRealName()));
            return this;
        }

        public MapBuilder withPassword(String ... password) {
            modelProperties.put("password", BaseModel.first(password, getPassword()));
            return this;
        }

        public MapBuilder withUserType(Integer ... userType) {
            modelProperties.put("userType", BaseModel.first(userType, getUserType()));
            return this;
        }

        public MapBuilder withNickName(String ... nickName) {
            modelProperties.put("nickName", BaseModel.first(nickName, getNickName()));
            return this;
        }

        public MapBuilder withMobilePhone(String ... mobilePhone) {
            modelProperties.put("mobilePhone", BaseModel.first(mobilePhone, getMobilePhone()));
            return this;
        }

        public MapBuilder withEmail(String ... email) {
            modelProperties.put("email", BaseModel.first(email, getEmail()));
            return this;
        }

        public MapBuilder withUserIcon(String ... userIcon) {
            modelProperties.put("userIcon", BaseModel.first(userIcon, getUserIcon()));
            return this;
        }

        public MapBuilder withOrgId(Long ... orgId) {
            modelProperties.put("orgId", BaseModel.first(orgId, getOrgId()));
            return this;
        }
        
        public MapBuilder withStatus(Integer ... status) {
            modelProperties.put("status", BaseModel.first(status, getStatus()));
            return this;
        }

        public MapBuilder withLastLoginTime(String ... lastLoginTime) {
            modelProperties.put("lastLoginTime", BaseModel.first(lastLoginTime, getLastLoginTime()));
            return this;
        }

        public MapBuilder withLoginTimes(Integer ... loginTimes) {
            modelProperties.put("loginTimes", BaseModel.first(loginTimes, getLoginTimes()));
            return this;
        }
        
        public MapBuilder withCreateTime(String ... createTime) {
            modelProperties.put("createTime", BaseModel.first(createTime, getCreateTime()));
            return this;
        }

        public MapBuilder withCreateBy(Long ... createBy) {
            modelProperties.put("createBy", BaseModel.first(createBy, getCreateBy()));
            return this;
        }

        public MapBuilder withUpdateTime(String ... updateTime) {
            modelProperties.put("updateTime", BaseModel.first(updateTime, getUpdateTime()));
            return this;
        }

        public MapBuilder withUpdateBy(Long ... updateBy) {
            modelProperties.put("updateBy", BaseModel.first(updateBy, getUpdateBy()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}