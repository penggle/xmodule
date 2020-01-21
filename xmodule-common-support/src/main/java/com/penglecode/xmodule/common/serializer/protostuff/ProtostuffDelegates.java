package com.penglecode.xmodule.common.serializer.protostuff;

import java.io.IOException;
import java.util.Locale;

import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Pipe;
import io.protostuff.WireFormat.FieldType;
import io.protostuff.runtime.Delegate;

public class ProtostuffDelegates {

    public static class LocaleDelegate implements Delegate<Locale> {

		@Override
		public FieldType getFieldType() {
			return FieldType.STRING;
		}

		@Override
		public Locale readFrom(Input input) throws IOException {
			String str = StringUtils.defaultIfEmpty(input.readString());
			String[] arr = str.split("\\_");
			String language = ArrayUtils.isEmpty(arr) ? "" : arr[0];
			String country = ArrayUtils.isEmpty(arr) ? "" : (arr.length >= 2 ? arr[1] : "");
            return new Locale(language, country);
		}

		@Override
		public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
			output.writeString(number, input.readString(), repeated);
		}

		@Override
		public Class<?> typeClass() {
			return Locale.class;
		}

		@Override
		public void writeTo(Output output, int number, Locale value, boolean repeated) throws IOException {
			output.writeString(number, StringUtils.defaultIfEmpty(value.getLanguage()) + "_" + StringUtils.defaultIfEmpty(value.getCountry()), repeated);
		}
    }
	
}
