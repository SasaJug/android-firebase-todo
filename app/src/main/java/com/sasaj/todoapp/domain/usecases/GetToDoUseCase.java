package com.sasaj.todoapp.domain.usecases;

import com.sasaj.todoapp.domain.Repository;
import com.sasaj.todoapp.domain.common.Transformer;
import com.sasaj.todoapp.domain.entities.ToDo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

public class GetToDoUseCase  extends UseCase<ToDo> {

    private static final String PARAM_TODO_KEY = ":todo_key";

    private Repository repository;

    public GetToDoUseCase(Repository repository, Transformer transformer) {
        super(transformer);
        this.repository = repository;
    }

    public Observable<ToDo> getTodo(@Nullable String todoKey){
        Map<String, Object> data = new HashMap<>();
        data.put(PARAM_TODO_KEY, todoKey);
        return observable(data);
    }


    @Override
    Observable<ToDo> createObservable(Map<String, Object> data) {
        return repository.getTodo((String)data.get(PARAM_TODO_KEY));
    }
}
