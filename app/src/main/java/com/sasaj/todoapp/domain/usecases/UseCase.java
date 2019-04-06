package com.sasaj.todoapp.domain.usecases;

import com.sasaj.todoapp.domain.common.Transformer;

import java.util.Map;

import io.reactivex.Observable;

public abstract class UseCase<T> {
    Transformer transformer;

    public UseCase(Transformer transformer){
        this.transformer = transformer;
    }

    abstract Observable<T> createObservable(Map<String, Object> data);

    Observable<T> observable( Map<String, Object> withData) {
        return createObservable(withData).compose(transformer);
    }

}
