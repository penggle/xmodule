# xmodule-examples-security-oauth2-login: 基于Spring-Security的OAuth2Login示例

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
public class OAuth2LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginController.class);

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@Resource(name = "defaultRestTemplate")
	private RestTemplate restTemplate;

	@RequestMapping(value="/login/success")
	public String loginSuccess(Model model, OAuth2AuthenticationToken authentication) {
		return "redirect:/index";
	}
	
	@RequestMapping(value="/index")
	public String index(Model model, OAuth2AuthenticationToken authentication) {
		OAuth2AuthorizedClient client = authorizedClientService
				.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

		if (!StringUtils.isEmpty(userInfoEndpointUri)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());

			HttpEntity<String> entity = new HttpEntity<String>("", headers);

			ResponseEntity<Map<String, Object>> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET,
					entity, new ParameterizedTypeReference<Map<String, Object>>() {
					});
			Map<String, Object> userInfo = response.getBody();
			LOGGER.debug(">>> userInfo = {}", userInfo);
			model.addAttribute("name", userInfo.get("name"));
		}
		return "index";
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

### 3.运行&测试

启动SpringBoot应用，在浏览器输入http://127.0.0.1:8081/login后选择"Google", 并进行登录