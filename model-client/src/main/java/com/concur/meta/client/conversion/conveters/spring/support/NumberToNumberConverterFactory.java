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

import com.concur.meta.client.utils.NumberUtils;
import com.concur.meta.client.conversion.conveters.spring.TypeDescriptor;
import com.concur.meta.client.conversion.conveters.spring.converter.ConditionalConverter;
import com.concur.meta.client.conversion.conveters.spring.converter.Converter;
import com.concur.meta.client.conversion.conveters.spring.converter.ConverterFactory;

/**
 * Converts from any JDK-standard Number implementation to any other JDK-standard Number implementation.
 *
 * <p>Support Number classes including Byte, Short, Integer, Float, Double, Long, BigInteger, BigDecimal. This class
 * delegates to {@link NumberUtils#convertNumberToTargetClass(Number, Class)} to perform the conversion.
 *
 * @author Keith Donald
 * @since 3.0
 * @see Byte
 * @see Short
 * @see Integer
 * @see Long
 * @see java.math.BigInteger
 * @see Float
 * @see Double
 * @see java.math.BigDecimal
 * @see NumberUtils
 */
final class NumberToNumberConverterFactory implements ConverterFactory<Number, Number>,
    ConditionalConverter {

	@Override
	public <T extends Number> Converter<Number, T> getConverter(Class<T> targetType) {
		return new NumberToNumber<T>(targetType);
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return !sourceType.equals(targetType);
	}

	private final static class NumberToNumber<T extends Number> implements Converter<Number, T> {

		private final Class<T> targetType;

		public NumberToNumber(Class<T> targetType) {
			this.targetType = targetType;
		}

		@Override
		public T convert(Number source) {
			return NumberUtils.convertNumberToTargetClass(source, this.targetType);
		}
	}

}
