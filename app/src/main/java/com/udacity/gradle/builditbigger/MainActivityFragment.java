package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myandroidlibrary.MainAndroidLibraryActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;


import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ProgressBar loading;
    private Button jokeButton;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        jokeButton = root.findViewById(R.id.btn_jokes);
        loading = root.findViewById(R.id.pb_loading);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        jokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((MainActivity) getActivity()).getIdlingResource() != null) {
                    (((MainActivity) getActivity())).setIdlingState(false);
                }
//                Toast.makeText(getActivity(), joker.getJoke(), Toast.LENGTH_SHORT).show();
                new EndpointsAsyncTask().execute(new Pair<Context, String>(getActivity(), "name"));

            }
        });
        return root;
    }

    class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private MyApi myApiService = null;
        private Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://192.168.1.8:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            context = params[0].first;
            String joke = params[0].second;

            try {
                return myApiService.sayHi(joke).execute().getData();
            } catch (IOException e) {
                return null;

            }
        }

        @Override
        protected void onPostExecute(String result) {
            loading.setVisibility(View.GONE);
            if (((MainActivity) getActivity()).getIdlingResource() != null) {
                (((MainActivity) getActivity())).setIdlingState(true);
            }
            if (result != null) {
                Intent androidLibrary = new Intent(getActivity(), MainAndroidLibraryActivity.class);
                androidLibrary.putExtra(getString(R.string.joke), result);
                getActivity().startActivity(androidLibrary);
            } else {
                Toast.makeText(context, "connection error", Toast.LENGTH_LONG).show();
            }
//            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }


}
