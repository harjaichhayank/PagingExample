package com.example.pagingExample;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<Integer, Item> {
    private Context context;

    static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private static final String SITE_NAME = "stackoverflow";

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Item> callback) {

        RetrofitClient.getInsance()
                .getApi()
                .getAnswers(FIRST_PAGE, PAGE_SIZE, SITE_NAME)
                .enqueue(new Callback<StackApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StackApiResponse> call, @NonNull Response<StackApiResponse> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body().items, null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StackApiResponse> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Load Initial Has Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        RetrofitClient.getInsance()
                .getApi()
                .getAnswers(params.key, PAGE_SIZE, SITE_NAME)
                .enqueue(new Callback<StackApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StackApiResponse> call, @NonNull Response<StackApiResponse> response) {
                        if (response.body() != null) {
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().items, key);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<StackApiResponse> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Load Before Has Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        RetrofitClient.getInsance()
                .getApi()
                .getAnswers(params.key, PAGE_SIZE, SITE_NAME)
                .enqueue(new Callback<StackApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StackApiResponse> call, @NonNull Response<StackApiResponse> response) {
                        if (response.body() != null) {
                            Integer key = response.body().has_more ? params.key + 1 : null;
                            callback.onResult(response.body().items, key);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<StackApiResponse> call, @NonNull Throwable t) {
                        Toast.makeText(context, "Load After Has Failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}

