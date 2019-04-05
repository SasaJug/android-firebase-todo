package com.sasaj.todoapp.domain.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public abstract class Mapper <E, T>{

    abstract T mapFrom(E from);

    Observable<T> observable(E from){
        return Observable.fromCallable(() -> mapFrom(from));
    }

    Observable<List<T>>  observable(List<E> from){
        return Observable.fromCallable(() -> {
            List<T> mappedList = new ArrayList<>();
            for(E element : from){
                mappedList.add(mapFrom(element));
            }
            return mappedList;
        });
    }

}
