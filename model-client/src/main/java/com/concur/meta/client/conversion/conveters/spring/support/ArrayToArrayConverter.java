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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.concur.meta.client.conversion.conveters.spring.ConversionService;
import com.concur.meta.client.utils.ObjectUtils;
import com.concur.meta.client.conversion.conveters.spring.TypeDescriptor;
import com.concur.meta.client.conversion.conveters.spring.converter.ConditionalGenericConverter;

/**
 * Converts an Array to another Array. First adapts the source array to a List, then
 * delegates to {@link CollectionToArrayConverter} to perform the target array conversion.
 *
 * @author Keith Donald
 * @author Phillip Webb
 * @since 3.0
 */
final class ArrayToArrayConverter implements ConditionalGenericConverter {

	private final CollectionToArrayConverter helperConverter;

	private final ConversionService conversionService;

	public ArrayToArrayConverter(ConversionService conversionService) {
		this.helperConverter = new CollectionToArrayConverter(conversionService);
		this.conversionService = conversionService;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object[].class, Object[].class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return this.helperConverter.matches(sourceType, targetType);
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType,
			TypeDescriptor targetType) {
		if ((conversionService instanceof GenericConversionService)
				&& ((GenericConversionService) conversionService).canBypassConvert(
						sourceType.getElementTypeDescriptor(),
						targetType.getElementTypeDescriptor())) {
			return source;
		}
		List<Object> sourceList = Arrays.asList(ObjectUtils.toObjectArray(source));
		return this.helperConverter.convert(sourceList, sourceType, targetType);
	}

}
