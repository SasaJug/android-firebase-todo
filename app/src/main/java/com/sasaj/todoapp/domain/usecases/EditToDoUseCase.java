package com.sasaj.todoapp.domain.usecases;

import com.sasaj.todoapp.domain.Repository;
import com.sasaj.todoapp.domain.common.Transformer;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

public class EditToDoUseCase extends UseCase<Object> {

    private static final String PARAM_TODO_KEY = ":todo_key";
    private static final String PARAM_TODO_TITLE = ":todo_title";
    private static final String PARAM_TODO_DESCRIPTION = ":todo_description";
    private static final String PARAM_TODO_COMPLETED = ":todo_completed";

    private Repository repository;

    public EditToDoUseCase(Repository repository, Transformer transformer) {
        super(transformer);
        this.repository = repository;
    }

    public Observable<Object> editTodo(@Nullable String todoKey, String title, String description, boolean completed) {
        Map<String, Object> data = new HashMap<>();
        data.put(PARAM_TODO_KEY, todoKey);
        data.put(PARAM_TODO_TITLE, title);
        data.put(PARAM_TODO_DESCRIPTION, description);
        data.put(PARAM_TODO_COMPLETED, completed);
        return observable(data);
    }

    @Override
    Observable<Object> createObservable(Map<String, Object> data) {
        return repository.editTodo((String) data.get(PARAM_TODO_KEY),
                (String) data.get(PARAM_TODO_TITLE),
                (String) data.get(PARAM_TODO_DESCRIPTION),
                (Boolean) data.get(PARAM_TODO_COMPLETED));
    }
}
