package org.pwc.todo.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi chatOpenApi() {
		String[] paths = {"/api/**"};

		return GroupedOpenApi.builder()
			.group("API v.1.0")
			.pathsToMatch(paths)
			.build();
	}
}
