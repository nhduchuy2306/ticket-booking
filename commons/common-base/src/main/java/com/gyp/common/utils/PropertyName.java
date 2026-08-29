package com.gyp.common.utils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Supplier;

public final class PropertyName {
	public static <T> String of(SerializableSupplier<T> methodReference) {
		try {
			Method writeReplace = methodReference.getClass().getDeclaredMethod("writeReplace");
			writeReplace.setAccessible(true);
			SerializedLambda invokeMethod = (SerializedLambda)writeReplace.invoke(methodReference);
			writeReplace.setAccessible(false);
			return extractPropertyName(invokeMethod.getImplMethodName());
		} catch(Exception e) {
			throw new RuntimeException("Failed to extract property name from supplier", e);
		}
	}

	// Add new method for method references
	public static <T, R> String of(SerializableFunction<T, R> methodReference) {
		try {
			Method writeReplace = methodReference.getClass().getDeclaredMethod("writeReplace");
			writeReplace.setAccessible(true);
			SerializedLambda invokeMethod = (SerializedLambda)writeReplace.invoke(methodReference);
			writeReplace.setAccessible(false);
			return extractPropertyName(invokeMethod.getImplMethodName());
		} catch(Exception e) {
			throw new RuntimeException("Failed to extract property name from method reference", e);
		}
	}

	private static String extractPropertyName(String methodName) {
		int startIndex = 0;
		if(methodName.startsWith("get")) {
			startIndex = 3;
		} else if(methodName.startsWith("is")) {
			startIndex = 2;
		}
		if(startIndex < methodName.length()) {
			return Character.toLowerCase(methodName.charAt(startIndex)) + methodName.substring(startIndex + 1);
		}
		return methodName; // Fallback if no prefix is found
	}

	@FunctionalInterface
	public interface SerializableSupplier<T> extends Supplier<T>, Serializable {
	}

	@FunctionalInterface
	public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
	}
}
