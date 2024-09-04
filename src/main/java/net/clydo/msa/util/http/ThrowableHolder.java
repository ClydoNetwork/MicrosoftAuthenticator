package net.clydo.msa.util.http;

@FunctionalInterface
public interface ThrowableHolder<T> {
    T get() throws Throwable;
}
