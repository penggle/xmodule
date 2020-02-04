# xmodule-examples-security-oauth2-sample-auth: 基于spring-security-oauth2-client、keycloak构建的OAuth2认证授权示例

## 示例功能实现

### 1. 基于OAuth2 password模式的用户登录.

- 示例见com.penglecode.xmodule.security.oauth2.examples.web.controller.OAuth2AuthApiController

### 2. 基于OAuth2 client_credentials模式的应用API之间相互调用的鉴权.

- 示例见com.penglecode.xmodule.security.oauth2.examples.web.controller.JokeApiConsumerController

### 3. 拓展了基于Redis存储的OAuth2AuthorizedClientService

- 代码见 xmodule-common-oauth2/com.penglecode.xmodule.common.security.oauth2.client.service.RedisOAuth2AuthorizedClientService.java

### 4. 关于OAuth2AuthorizedClientRepository的默认实现AuthenticatedPrincipalOAuth2AuthorizedClientRepository的问题

- 阅读AuthenticatedPrincipalOAuth2AuthorizedClientRepository代码可知，其根据用户是否匿名，将OAuth2AuthorizedClient分两处存储：一处存储在内存中(InMemoryOAuth2AuthorizedClientService)，一处存储在HttpSession中(HttpSessionOAuth2AuthorizedClientRepository). 通过：1、使用自定义的com.penglecode.xmodule.common.security.oauth2.client.support.OAuth2PrincipalNameAuthentication && 2、principalName使用username(password模式)或者client_id(client_credentials模式)来替代 && 3、自定义Keycloak JWT令牌的claims.sub值(在password模式使用username或者client_credentials模式下使用client_id)。通过以上三点共通确立OAuth2AuthorizedClient的存储和获取的唯一性(正确性). 至于为何要如此确保? 原因在于org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken中的principalName使用了硬编码：name取自令牌的sub字段导致的

## 运行&测试

1. 先启动xmodule-examples-security-oauth2-sample-auth (8081端口)

启动时在com.penglecode.xmodule.security.oauth2.examples.initializer.OAuth2AuthWebAppStartupInitializer初始化类中根据需要自动创建keycloak的realm

2. 再启动xmodule-examples-security-oauth2-sample-api (8082端口)

3. GET请求接口：http://127.0.0.1:8081/api/keycloak/init (匿名可访问)，用于重新初始化示例中需要用到的Keycloak的Clients、ClientScopes、Roles、Users等

4. 测试OAuth2登录接口：com.penglecode.xmodule.security.oauth2.examples.web.controller.OAuth2AuthApiController.login(...)

5. 测试OAuth2续约接口：com.penglecode.xmodule.security.oauth2.examples.web.controller.OAuth2AuthApiController.renewal(...)

6. 测试OAuth2用户信息接口：com.penglecode.xmodule.security.oauth2.examples.web.controller.OAuth2AuthApiController.userinfo(...)

7. 测试应用之间相互调用示例：com.penglecode.xmodule.security.oauth2.examples.web.controller.JokeApiConsumerController.getJokeList(...)