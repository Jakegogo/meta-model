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

import com.concur.meta.client.conversion.conveters.spring.ConversionService;
import com.concur.meta.client.conversion.conveters.spring.converter.Converter;
import com.concur.meta.client.utils.ClassUtils;
import com.concur.meta.client.conversion.conveters.spring.TypeDescriptor;
import com.concur.meta.client.conversion.conveters.spring.converter.ConditionalConverter;

/**
 * Calls {@link Enum#name()} to convert a source Enum to a String.  This converter will
 * not match enums with interfaces that can be converterd.
 * @author Keith Donald
 * @author Phillip Webb
 * @since 3.0
 */
final class EnumToStringConverter implements Converter<Enum<?>, String>, ConditionalConverter {

	private final ConversionService conversionService;

	public EnumToStringConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		for (Class<?> interfaceType : ClassUtils.getAllInterfacesForClass(sourceType.getType())) {
			if (conversionService.canConvert(TypeDescriptor.valueOf(interfaceType), targetType)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String convert(Enum<?> source) {
		return source.name();
	}

}
