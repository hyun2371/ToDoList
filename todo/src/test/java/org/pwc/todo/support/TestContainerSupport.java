package org.pwc.todo.support;


import static lombok.AccessLevel.*;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PROTECTED)
public abstract class TestContainerSupport {

	private static final String MYSQL_IMAGE = "mysql:8.0";
	private static final int MYSQL_PORT = 3306;

	private static final JdbcDatabaseContainer<?> MYSQL;

	// 컨테이너 싱글톤으로 생성
	static {
		MYSQL = new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE))
			.withExposedPorts(MYSQL_PORT)
			.withReuse(true);
		MYSQL.start();
	}

	// 동적으로 속성 할당
	@DynamicPropertySource
	public static void setUp(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
		registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL::getUsername);
		registry.add("spring.datasource.password", MYSQL::getPassword);
	}
}
