package com.sasaj.todoapp.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sasaj.todoapp.data.exceptions.FirebaseRxDataCastException;
import com.sasaj.todoapp.data.exceptions.FirebaseRxDataException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RxFirebase {
    private static final String TAG = RxFirebase.class.getName();

    /**
     * As some Firebase {@link Task} are returning a Void (null) element and RxJava 2.X no longer
     * accept null values, we need to emit a different object
     * https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#nulls
     */
    public static class FirebaseTaskResponseSuccess {
    }

    @NonNull
    public static <T> Observable<T> getObservableForSingleEvent(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(emitter -> query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, dataSnapshot.toString());
                T value = dataSnapshot.getValue(clazz);
                if (value != null) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(value);
                    }
                } else {
                    if (!emitter.isDisposed()) {
                        emitter.onError(new FirebaseRxDataCastException("Unable to cast Firebase data response to " + clazz.getSimpleName()));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new FirebaseRxDataException(error));
                }
            }
        }));
    }

    @NonNull
    public static <T> Observable<T> getObservable(@NonNull final Query query, @NonNull final Class<T> clazz) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final ObservableEmitter<T> emitter) throws Exception {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, dataSnapshot.toString());
                        T value = dataSnapshot.getValue(clazz);
                        if (value != null) {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(value);
                            }
                        } else {
                            query.removeEventListener(this);
                            if (!emitter.isDisposed()) {
                                emitter.onError(new FirebaseRxDataCastException("Unable to cast Firebase data response to " + clazz.getSimpleName()));
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        query.removeEventListener(this);
                        if (!emitter.isDisposed()) {
                            emitter.onError(new FirebaseRxDataException(error));
                        }
                    }
                });
            }
        });
    }

    @NonNull
    public static <T> Observable<T> getObservable(@NonNull final Task<T> task) {
        return Observable.create(emitter -> task.addOnSuccessListener(result -> {
            if (!emitter.isDisposed()) {
                emitter.onNext(result);
            }
        }).addOnFailureListener(e -> {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }));
    }

    /**
     * As RxJava 2 does not support Void (null) values as emitter item, we have to send an object back
     * @param task
     * @param objectToReturn
     * @param <T>
     * @return
     */
    @NonNull
    public static <T> Observable<Object> getObservable(@NonNull final Task<T> task,
                                                       @NonNull final Object objectToReturn) {
        return Observable.create(emitter -> task.addOnSuccessListener((OnSuccessListener<Object>) result -> {
            if (!emitter.isDisposed()) {
                if ((result instanceof Void
                        || result == null)
                        && objectToReturn != null) {
                    emitter.onNext(objectToReturn);
                } else {
                    emitter.onNext(result);
                }
            }
        }).addOnFailureListener(e -> {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }));
    }
}