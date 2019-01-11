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

import java.util.function.Supplier;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.mongodb.inbound.MongoDbMessageSource;
import org.springframework.messaging.Message;

@EnableConfigurationProperties({ MongoEventSourceProperties.class })
public class MongoSupplier implements Supplier<String> {

	private final MongoDbMessageSource mongoSource;

	public MongoSupplier(MongoDbMessageSource mongoSource) {
		this.mongoSource = mongoSource;
	}

	@Override
	public String get() {
		Message<?> message = this.mongoSource.receive();
		return (message != null) ? message.getPayload().toString() : null;
	}
}
