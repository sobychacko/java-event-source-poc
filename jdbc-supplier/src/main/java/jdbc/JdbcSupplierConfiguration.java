/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;

@Configuration
@EnableConfigurationProperties(JdbcEventSourceProperties.class)
public class JdbcSupplierConfiguration {

	@Autowired
	JdbcEventSourceProperties properties;

	@Autowired
	private DataSource dataSource;

	@Bean
	public MessageSource<Object> jdbcMessageSource() {
		JdbcPollingChannelAdapter jdbcPollingChannelAdapter =
				new JdbcPollingChannelAdapter(this.dataSource, this.properties.getQuery());
		jdbcPollingChannelAdapter.setMaxRowsPerPoll(this.properties.getMaxRowsPerPoll());
		jdbcPollingChannelAdapter.setUpdateSql(this.properties.getUpdate());
		return jdbcPollingChannelAdapter;
	}

	@Bean
	public JdbcSupplier jdbcSupplier(MessageSource messageSource) {
		return new JdbcSupplier(messageSource);
	}
}
