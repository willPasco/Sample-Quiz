package com.android.samplequiz.di;

import com.android.samplequiz.service.QuizService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String BASE_URL = "34.73.190.231:8080";

    @Provides
    @Singleton
    public HttpLoggingInterceptor povideInterceptor() {
        return new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    public Gson provideGsonBuilder() {
        return new GsonBuilder().setDateFormat(DATE_FORMAT).create();
    }

    @Provides
    @Singleton
    public GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    public OkHttpClient provideClient(HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }
    
    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory factory){
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(factory)
                .baseUrl(BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    public QuizService provideQuizService(Retrofit retrofit){
        return retrofit.create(QuizService.class);
    }
}
