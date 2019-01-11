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

package io.projectriff.source;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.core.FluxSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author Mark Fisher
 */
@Component
public class EventEmitter implements SmartLifecycle, ApplicationContextAware {

	private final String url;

	private final Duration duration;

	private ConfigurableApplicationContext context;

	private Disposable subscription;

	private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	public EventEmitter(EventSourceProperties properties) {
		this.url = properties.getTarget();
		long period = properties.getPeriod();
		if (period > 0) {
			this.duration = Duration.ofMillis(period);
		}
		else {
			this.duration = null;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = (ConfigurableApplicationContext) context;
	}

	@Override
	public void start() {
		FunctionCatalog catalog = this.context.getBean(FunctionCatalog.class);
		FluxSupplier<?> supplier = (FluxSupplier<?>) catalog.lookup(Supplier.class, "function0");
		if (this.duration != null) {
			supplier = new FluxSupplier<>(supplier.getTarget(), this.duration);
		}
		Flux<?> flux = supplier.get();
		this.subscription = flux.log().subscribeOn(Schedulers.single()).doOnComplete(() -> stop())
				.subscribe(event -> this.restTemplate.postForEntity(url, event, Object.class));
		if (this.duration == null) {
			flux.timeout(Duration.ofMillis(1000));
		}
	}

	@Override
	public void stop() {
		this.subscription.dispose();
		this.context.close();
	}

	@Override
	public boolean isRunning() {
		return !(this.subscription == null || this.subscription.isDisposed());
	}
}
