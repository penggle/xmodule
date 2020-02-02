# xmodule-examples-security-oauth2-login-sso: 基于Spring-Security的OAuth2Login示例

## SpringSecurity官方[参考示例](https://github.com/spring-projects/spring-security/tree/5.2.1.RELEASE/samples/boot/oauth2login)

## Quickstart

### 1. 引入Maven依赖

```xml

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-client</artifactId>
</dependency>
<dependency>
   <groupId>org.springframework.security</groupId>
   <artifactId>spring-security-oauth2-jose</artifactId>
</dependency>

```

### 2.OAuth2客户端设置

#### 2.1 获取客户端凭证(client credentials)

- 获取Google的OAuth2客户端凭证：[Google API Console](https://console.developers.google.com/) - "Credentials"这一节中获取clientId与clientSecret。
- 获取Github的OAuth2客户端凭证：[Register a new OAuth application](https://github.com/settings/applications/new)
- 关于keycloak的配置，请参阅：[keycloak-springsecurity5-sample](https://github.com/hantsy/keycloak-springsecurity5-sample)

- 关于Ory/Hydra的配置，请参阅：[ORY Hydra文档](https://www.ory.sh/docs/hydra/)

#### 2.2 配置application.yaml

```yaml
spring:
	#OAuth2客户端配置
    security:
        oauth2:
            client:
                registration:
                    google:
                        client-id: <Your clientId>
                        client-secret: <Your clientSecret>
```

由于SpringBoot2.x自动配置了主流的OAuth2 Provider([CommonOAuth2Provider.java](https://github.com/spring-projects/spring-security/blob/master/config/src/main/java/org/springframework/security/config/oauth2/client/CommonOAuth2Provider.java))，因此此处省略了其他的配置(如authorizationUri、tokenUri、jwkSetUri、userInfoUri等)

#### 2.3 spring-security配置

```java
@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
         		.anyRequest().authenticated()
         	.and()
         		.oauth2Login()
         		.defaultSuccessUrl("/login/success");
    }
	
}
```

#### 2.4 登录成功后的页面配置

SpringMVC配置:

```java
@Controller
public class OAuth2LoginController implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginController.class);

	private final Map<String,String> oauth2LoginLinks = new LinkedHashMap<String,String>();
	
	@GetMapping(value="/login")
	public String login(Model model) {
		model.addAttribute("oauth2LoginLinks", oauth2LoginLinks);
		return "login";
	}
	
	@RequestMapping(value="/login/failure")
	public String loginFailure(HttpServletRequest request, HttpServletResponse response, Model model) {
		Exception exception = SpringSecurityUtils.getAuthenticationException(request);
		LOGGER.error(String.format(">>> login failure: %s", exception.getMessage()), exception);
		model.addAttribute("error", Boolean.TRUE);
		model.addAttribute("message", ExceptionUtils.getRootCauseMessage(exception));
		return "login";
	}
	
	@RequestMapping(value="/login/success")
	public String loginSuccess() {
		return "redirect:/index";
	}
	
	@RequestMapping(value="/index")
	public String index(Model model, AbstractAuthenticationToken authentication) {
		Map<String,Object> userInfo = null;
		LOGGER.info(">>> authentication = {}", authentication);
		if(authentication instanceof OAuth2AuthenticationToken) {
			LOGGER.info(">>> OAuth2 User Login Success!");
			OAuth2AuthenticationToken authentication0 = SpringSecurityUtils.getAuthentication();
			System.out.println(authentication0 == authentication);
			userInfo = OAuth2UserUtils.getCurrentOAuth2UserInfo();
			OAuth2AccessToken accessToken = OAuth2ClientUtils.getCurrentOAuth2AccessToken();
			LOGGER.debug(">>> userInfo = {}", JsonUtils.object2Json(userInfo));
			LOGGER.debug(">>> accessToken = {}", JsonUtils.object2Json(accessToken));
		} else {
			LOGGER.info(">>> Local User Login Success!");
			User user = (User) authentication.getPrincipal();
			userInfo = new LinkedHashMap<String,Object>();
			userInfo.put("name", user.getUsername());
			userInfo.put("authorities", user.getAuthorities());
			LOGGER.debug(">>> userInfo = {}", JsonUtils.object2Json(userInfo));
		}
		
		model.addAttribute("userInfo", userInfo);
		return "index";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String,String> loginLinks = OAuth2LoginUtils.getConfiguredOAuth2LoginLinks();
		if(loginLinks != null) {
			oauth2LoginLinks.putAll(loginLinks);
		}
	}

}
```

JSP页面配置:

src/main/resources/META-INF/resources/WEB-INF/jsp/index.jsp

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
	<h1>首页</h1>
	<div>
		<a href="${pageContext.request.contextPath}/logout" style="float:right;">退出</a>
	</div>
	<div>
		<h1>Welcome ${name}!</h1>
	</div>
</body>
</html>
```

#### 2.5 ORY Hydra登录与同意页面的提供

由于ORY Hydra没有用户模块，因此也就没有用户登录及同意页面的提供，需要额外开发。
具体使用spring-security开发的案例参见隔壁项目：xmodule-examples-security-oauth2-hydra-login(该项目模仿自[官方示例](https://github.com/ory/hydra-login-consent-node))

#### 2.6 SSO应用的初始化接口(匿名可访问)

ORY Hydra客户端初始化：com.penglecode.xmodule.security.oauth2.examples.web.controller.OAuth2HydraServerController
Keycloak客户端初始化：com.penglecode.xmodule.security.oauth2.examples.web.controller.OAuth2KeycloakServerController

### 3.运行&测试

启动SpringBoot应用，在浏览器输入http://127.0.0.1:8081/login后选择"Google", 并进行登录