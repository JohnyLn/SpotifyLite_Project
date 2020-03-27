package fr.esilv.spotifylite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import fr.esilv.spotifylite.adapters.SpotifySearchItemAdapter;
import fr.esilv.spotifylite.models.SpotifySearchItem;
import fr.esilv.spotifylite.models.SpotifySearchResponse;
import retrofit2.Retrofit;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;


//import okhttp3.OkHttpClient;






public class SearchFragment extends Fragment {


    private RecyclerView recyclerView;
    private SpotifyService service;
    Button searchbtn;
    private EditText editText;
    private TextView tokenView ;


    private static final String CLIENT_ID = "3b5446bb2fee48b787fef966f279592d";
    private static final String REDIRECT_URI = "fr.esilv.spotifylite://callback";
    private static  String OAUTH_TOKEN;
    private String mAccessCode;



    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;

    //okHttpClient
    //private OkHttpClient mOkHttpClient ;
    //private okhttp3.Call mCall;

    private TextView textView;

    private String text;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";




    //Recycler View in a fragment :https://www.youtube.com/watch?v=9I2jUY0mwYQ
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        searchbtn = v.findViewById(R.id.button_search);
        editText = v.findViewById(R.id.searchtext);
        searchbtn.setEnabled(false);

        tokenView = v.findViewById(R.id.token_text_view);

        textView = v.findViewById(R.id.textview);



        //Prof______________________________
        //recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //_________________________

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        //We use retrofit
        //Base URL https://developer.spotify.com/console/get-track/?id=&market=
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

       /* final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/")
                .addHeader("Authorization","Bearer " + OAUTH_TOKEN)
                .build();
                */


        service = retrofit.create(fr.esilv.spotifylite.SpotifyService.class);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 0) {
                    searchbtn.setEnabled(true);
                } else {
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.length() >= 0) {
                    launchSearch(editText.getText().toString());
                    Log.d("SetOnClick", "onFinish");

                } else {
                    launchSearch("");
                    Log.d("SetOnClick", "onFinish Failed");

                }
                textView.setText(editText.getText().toString());
                saveData();
            }
        });

        loadData();
        updateView();


        return v;
    }


    public void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, textView.getText().toString());
        editor.commit();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
    }

    public void updateView(){
        textView.setText(text);
    }


    //Take the AUTH TOKEN REQUEST
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("SearchF","onActivityResu_Token");
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        if (response.getError() != null && response.getError().isEmpty()) {
            //setResponse(response.getError());
        }
        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            OAUTH_TOKEN = response.getAccessToken();
            tokenView.setText(getString(R.string.token, OAUTH_TOKEN));



        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            mAccessCode = response.getCode();
            //updateCodeView();
        }
    }
    */


    private void launchSearch(String query) {
        service.search(query, OAUTH_TOKEN).enqueue(new Callback<SpotifySearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpotifySearchResponse> call, @NonNull Response<SpotifySearchResponse> response) {
                Log.d("SearchFragment", "onResponse");
                if (response.isSuccessful()) {
                    SpotifySearchResponse SpotifySearchResponse = response.body();
                    editText.setText(response.toString());
                    List<SpotifySearchItem> itemList = SpotifySearchResponse.getItems();
                    recyclerView.setAdapter(new SpotifySearchItemAdapter(itemList));
                    Log.d("SearchFragment", "onSuccessful");

                }
            }

            @Override
            public void onFailure(Call<SpotifySearchResponse> call, Throwable t) {
                Log.e("SearchFragment", "onFailure", t);
            }
        });
    }





}
