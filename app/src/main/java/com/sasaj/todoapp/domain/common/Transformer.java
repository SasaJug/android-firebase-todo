package com.sasaj.todoapp.domain.common;

import io.reactivex.ObservableTransformer;

public abstract class Transformer<T> implements ObservableTransformer<T,T> {
}
