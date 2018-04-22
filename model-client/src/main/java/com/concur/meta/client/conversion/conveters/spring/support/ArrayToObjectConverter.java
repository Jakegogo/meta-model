/*
 * Copyright 2002-2012 the original author or authors.
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

package com.concur.meta.client.conversion.conveters.spring.support;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;

import com.concur.meta.client.conversion.conveters.spring.ConversionService;
import com.concur.meta.client.conversion.conveters.spring.TypeDescriptor;
import com.concur.meta.client.conversion.conveters.spring.converter.ConditionalGenericConverter;

/**
 * Converts an Array to an Object by returning the first array element after converting it to the desired targetType.
 *
 * @author Keith Donald
 * @since 3.0
 */
final class ArrayToObjectConverter implements ConditionalGenericConverter {

	private final ConversionService conversionService;

	public ArrayToObjectConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object[].class, Object.class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		if (sourceType.isAssignableTo(targetType)) {
			return source;
		}
		if (Array.getLength(source) == 0) {
			return null;
		}
		Object firstElement = Array.get(source, 0);
		return this.conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
	}

}
