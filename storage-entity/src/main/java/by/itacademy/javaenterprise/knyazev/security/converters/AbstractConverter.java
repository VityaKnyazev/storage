package by.itacademy.javaenterprise.knyazev.security.converters;

import java.util.function.Function;


public abstract class AbstractConverter<T, U> {
	private Function<T, U> fromOneToTwo;
	private Function<U, T> fromTwoToOne;
	
	AbstractConverter(Function<T, U> fromOneToTwo, Function<U, T> fromTwoToOne) {
		this.fromOneToTwo = fromOneToTwo;
		this.fromTwoToOne = fromTwoToOne;
	}
	
	public U convertFromOneToTwo(T two) {
		return fromOneToTwo.apply(two);
	}
	
	public T convertFromTwoToOne(U one) {
		return fromTwoToOne.apply(one);
	}	
}