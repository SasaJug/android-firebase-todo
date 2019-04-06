package com.sasaj.todoapp.domain.common;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

public class TestTransformer<T> extends Transformer {
    @Override
    public ObservableSource apply(Observable upstream) {
        return upstream;
    }
}
