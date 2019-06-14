package com.udacity.gradle.builditbigger.free;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myandroidlibrary.MainAndroidLibraryActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ProgressBar loading;
    private Button jokeButton,iAdd;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;




    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_free, container, false);
        jokeButton = root.findViewById(R.id.btn_jokes);
        iAdd = root.findViewById(R.id.btn_international_add);
        loading = root.findViewById(R.id.pb_loading);
        mAdView = root.findViewById(R.id.adView);

        ///////////
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getActivity().getResources().getString(R.string.fire_base_banner_test_international_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //////////////
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        jokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(), joker.getJoke(), Toast.LENGTH_SHORT).show();
                new EndpointsAsyncTask().execute(new Pair<Context, String>(getActivity(), "name"));

            }
        });
        iAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.international_add_error), Toast.LENGTH_SHORT).show();
                }
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

            if (result != null) {
                Intent androidLibrary = new Intent(getActivity(), MainAndroidLibraryActivity.class);
                androidLibrary.putExtra(getString(R.string.joke), result);
                getActivity().startActivity(androidLibrary);
            } else {
                Toast.makeText(context, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        }
    }


}
