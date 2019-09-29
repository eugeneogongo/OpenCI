/*
 * Created by Eugene on $file.created
 * Modified on $file.modified
 * Copyright (c) 2019.
 */

package com.openci.apicommunicator.restservices;

import com.openci.apicommunicator.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vicky on 08-01-2018.
 * Modified by Eugene Odera
 * Changes
 * Http Logging of Headers for debugging purposes only
 */

class APIClient {
    private static final String PUBLIC_BASE_URL = "https://api.travis-ci.org/";
    private static final String PRIVATE_BASE_URL = "https://api.travis-ci.com/";
    private static Retrofit publicRetroFit = null;
    private static Retrofit privateRetroFit = null;


    static Retrofit getPublicClient() {
        if (publicRetroFit == null) {
            publicRetroFit = new Retrofit.Builder()
                    .baseUrl(PUBLIC_BASE_URL)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return publicRetroFit;
    }

    static Retrofit getPrivateClient() {
        if (privateRetroFit == null) {
            privateRetroFit = new Retrofit.Builder()
                    .baseUrl(PRIVATE_BASE_URL)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return privateRetroFit;
    }

    /**
     * Enables Logging on Debug only
     *
     * @return client
     */
    private static OkHttpClient getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            //  log level
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            // add logging interceptor
            httpClient.addInterceptor(logging);
        }

        return httpClient.build();
    }
}
