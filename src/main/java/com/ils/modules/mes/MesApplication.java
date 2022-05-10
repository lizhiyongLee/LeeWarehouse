package com.ils.modules.mes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import com.ils.common.util.SpringContextUtils;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * MES 项目启动类
 * 
 * @author Conner
 * @date 2020/03/19
 */
@Slf4j
@EnableSwagger2
@EnableAsync
@ComponentScan(value = { "com.ils" })
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
public class MesApplication {

	public static void main(String[] args) throws UnknownHostException {
		System.setProperty("spring.devtools.restart.enabled", "true");

        ConfigurableApplicationContext application = SpringApplication.run(MesApplication.class, args);
		Environment env = application.getEnvironment();
		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = env.getProperty("server.port");
		String path = env.getProperty("server.servlet.context-path");
		log.info("\n----------------------------------------------------------\n\t"
            + "Application mes is running! Access URLs:\n\t" + "Local: \t\thttp://localhost:" + port + path
				+ "/\n\t" + "External: \thttp://" + ip + ":" + port + path + "/\n\t" + "swagger-ui: \thttp://" + ip
				+ ":" + port + path + "/swagger-ui.html\n\t" + "Doc: \t\thttp://" + ip + ":" + port + path
				+ "/doc.html\n" + "----------------------------------------------------------");
	}

    @Bean("iMesApi")
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				// 此包路径下的类，才生成接口文档
            .apis(RequestHandlerSelectors.basePackage("com.ils.modules.mes"))
				// 加了ApiOperation注解的类，才生成接口文档
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
				.build().securitySchemes(Collections.singletonList(SpringContextUtils.getBean(SecurityScheme.class)))
            .groupName("MES管理");
		// .globalOperationParameters(setHeaderToken());
	}

	/**
	 * api文档的详细信息函数,注意这里的注解引用的是哪个
	 *
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				// //大标题
				.title("iMES 后台服务API接口文档")
				// 版本号
				.version("1.0")
//				.termsOfServiceUrl("NO terms of service")
				// 描述
				.description("后台API接口")
				// 作者
				.contact(new Contact("ILS团队", "", "")).license("The Apache License, Version 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").build();
	}
	

}