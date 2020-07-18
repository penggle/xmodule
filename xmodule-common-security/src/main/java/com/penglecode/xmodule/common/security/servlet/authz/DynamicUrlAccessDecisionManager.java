package com.penglecode.xmodule.common.security.servlet.authz;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Collections;

/**
 * 自定义的动态URL-ROLE权限拦截AccessDecisionManager
 * 
 * @author 	pengpeng
 * @date	2019年5月20日 下午1:38:45
 */
public class DynamicUrlAccessDecisionManager extends AffirmativeBased {

    @Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		super.decide(authentication, object, configAttributes);
	}

	public DynamicUrlAccessDecisionManager() {
        super(Collections.singletonList(new RoleVoter()));
        setAllowIfAllAbstainDecisions(false);
    }

}