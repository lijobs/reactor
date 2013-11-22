/*
 * Copyright (c) 2011-2013 GoPivotal, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.operations;

import reactor.core.Observable;
import reactor.event.Event;
import reactor.function.Function;

/**
 * @author Stephane Maldini
 */
public class FlatMapOperation<T, V, E extends OperationPipe<V>> extends BaseOperation<T> {

	private final Function<T, E> fn;

	public FlatMapOperation(Function<T,E> fn, Observable d, Object successKey,
	                        Object failureKey) {
		super(d, successKey, failureKey);
		this.fn = fn;
	}

	@Override
	public void doOperation(Event<T> value) {
		E val = fn.apply(value.getData());
		val.addOperation(new ForwardOperation<V>(getObservable(), getSuccessKey(), getFailureKey()));
		val.flush();
	}
}
