package com.singh.daman.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daman on 11/9/16.
 */
public class MoviesFragment extends Fragment {

    private MoviesAdapter mMoviesAdapter;
    ArrayList<String> moviesposter, overview, date, title, vote, id;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Data();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        moviesposter = new ArrayList<String>();
        overview = new ArrayList<String>();
        date = new ArrayList<String>();
        title = new ArrayList<String>();
        vote = new ArrayList<String>();
        id = new ArrayList<String>();

        mMoviesAdapter = new MoviesAdapter(getActivity(), moviesposter, overview, date, title, vote);
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mMoviesAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_IMAGE", moviesposter.get(i));
                extras.putString("EXTRA_OVERVIEW", overview.get(i));
                extras.putString("EXTRA_DATE", date.get(i));
                extras.putString("EXTRA_TITLE", title.get(i));
                extras.putString("EXTRA_VOTE", vote.get(i));
                extras.putString("EXTRA_ID", id.get(i));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Data();
    }

    public void Data() {
        try {
            final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String API_KEY_URL = "api_key=";
            final String API_KEY = "78152e1f5dc1e0ca19063a06ea342fae";
            final String IMAGE_URL = "http://image.tmdb.org/t/p/w500";
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = prefs.getString(getString(R.string.pref_sort_key),
                    getString(R.string.pref_sort_popularity));
            String order = prefs.getString(getString(R.string.pref_order_key),
                    getString(R.string.pref_order_asc));

            String url = BASE_URL + API_KEY_URL + API_KEY + "&sort_by=" + sort + order;
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                moviesposter.clear();
                                JSONObject object = new JSONObject(response);
                                String syncresponse = object.getString("results");
                                JSONArray a1obj = new JSONArray(syncresponse);
                                for (int j = 0; j < a1obj.length(); j++) {
                                    JSONObject obj = a1obj.getJSONObject(j);
                                    String image = IMAGE_URL+obj.getString("poster_path");
                                    moviesposter.add(image);
                                    overview.add(obj.getString("overview"));
                                    title.add(obj.getString("title"));
                                    vote.add(obj.getString("vote_average"));
                                    date.add(obj.getString("release_date"));
                                    id.add(obj.getString("id"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mMoviesAdapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(),"No internet connections!",Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
