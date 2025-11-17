package org.pwc.todo.support;

import org.pwc.todo.common.config.QueryDslConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Repository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestAuditingConfig.class, QueryDslConfig.class})
public abstract class DataJpaTestSupport extends TestContainerSupport {
	protected final PageRequest pageRequest = PageRequest.of(0, 10);
}
