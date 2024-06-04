package com.paidservices.paidservicesdesktopapp.webclient.client;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.CompletableFuture;

class MainServiceCallback<T> implements Callback<T> {
    private final CompletableFuture<T> result;

    public MainServiceCallback(CompletableFuture<T> result) {
        this.result = result;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            result.complete(response.body());
            return;
        }

        if (response.code() == 500) {
            result.completeExceptionally(new RuntimeException("Неизвестная ошибка сервера"));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        result.completeExceptionally(throwable);
    }
}
