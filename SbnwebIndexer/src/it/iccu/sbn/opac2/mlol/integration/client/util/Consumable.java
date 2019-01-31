package it.iccu.sbn.opac2.mlol.integration.client.util;

import java.util.Objects;

public class Consumable<T> {

	private static final Consumable<?> EMPTY = new Consumable<>();
	
	private T value;
	
	@SuppressWarnings("unchecked")
	public static <T> Consumable<T> empty() {
		Consumable<T> t = (Consumable<T>) EMPTY;
		return t;
	}

	public static <T> Consumable<T> of(T value) {
		return new Consumable<>(value);
	}
	
    private Consumable(T value) {
        this.value = Objects.requireNonNull(value);
    }
    
    private Consumable() {
        this.value = null;;
    }
	
    public T consume() {
    	T tmp = value;
    	value = null;
        return tmp;
    }
    
    public T peek() {
        return value;
    }
    
    public boolean isPresent() {
        return value != null;
    }

}
