package org.pwc.todo.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest
public abstract class IntegrationTestSupport extends TestContainerSupport{
	@Autowired
	protected EntityManager em;
	protected final PageRequest pageRequest = PageRequest.of(0, 10);
}
