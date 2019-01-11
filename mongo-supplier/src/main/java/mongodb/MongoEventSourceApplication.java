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

package mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.mongodb.inbound.MongoDbMessageSource;

@SpringBootApplication
public class MongoEventSourceApplication {

	@Bean
	public MongoEventSourceProperties properties() {
		return new MongoEventSourceProperties();
	}

	@Bean
	public MongoDbMessageSource mongoSource(MongoEventSourceProperties properties, MongoTemplate mongoTemplate) {
		Expression queryExpression = new LiteralExpression(properties.getQuery());
		MongoDbMessageSource mongoSource = new MongoDbMessageSource(mongoTemplate, queryExpression);
		mongoSource.setCollectionNameExpression(new LiteralExpression(properties.getCollection()));
		mongoSource.setEntityClass(String.class);
		return mongoSource;
	}

	@Bean
	public MongoSupplier supplier(MongoDbMessageSource mongoSource) {
		return new MongoSupplier(mongoSource);
	}

	public static void main(String[] args) {
		SpringApplication.run(MongoEventSourceApplication.class, args);
	}
}
