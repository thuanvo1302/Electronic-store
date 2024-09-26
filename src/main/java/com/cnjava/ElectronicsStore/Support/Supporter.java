package com.cnjava.ElectronicsStore.Support;


import java.util.Collection;

public class Supporter<T> {
	public boolean isNullOrEmptyCollection(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}
}
