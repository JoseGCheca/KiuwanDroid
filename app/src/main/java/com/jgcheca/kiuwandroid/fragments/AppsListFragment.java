package com.jgcheca.kiuwandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jgcheca.kiuwandroid.R;
import com.jgcheca.kiuwandroid.Utils.Session;
import com.jgcheca.kiuwandroid.activities.AppDetailedActivity;
import com.jgcheca.kiuwandroid.rest.RestClient;
import com.jgcheca.kiuwandroid.rest.model.AppKiuwan;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;


public class AppsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    public View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout swipeContainer;
    RecyclerViewAdapter recyclerAdapter;



    /*public static AppsListFragment newInstance(String param1, String param2) {
        AppsListFragment fragment = new AppsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        if(view==null) {
            view = inflater.inflate(
                    R.layout.fragment_blank, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
            setupRecyclerView(recyclerView);

            String credentials = "";
            // String string = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            String string = "JSESSIONID=" + Session.get().getSessionId();

            RestClient.apiService.getApps("JSESSIONID=" + Session.get().getSessionId(), new Callback<JsonArray>() {
                @Override
                public void success(JsonArray appKiuwans, Response response) {
                    TypedInput body = response.getBody();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                        StringBuilder out = new StringBuilder();
                        String newLine = System.getProperty("line.separator");
                        String line;
                        while ((line = reader.readLine()) != null) {
                            out.append(line);
                            out.append(newLine);
                        }

                        // Prints the correct String representation of body.
                        Log.d("RETROFIT response", String.valueOf(out));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    recyclerAdapter = new RecyclerViewAdapter(appKiuwans);

                    Log.d("jsonarray response", appKiuwans.toString());
                    recyclerView.setAdapter(recyclerAdapter);
                  //  swipeContainer.setRefreshing(false);

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("AppsListError", "FAILURE");
                    Log.d("AppsListError", error.toString());

                    Log.d("Tipo de error", error.getKind().toString());
                }
            });

            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    fetchTimelineAsync(0);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

    }

        return view;

    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        RestClient.apiService.getApps("JSESSIONID=" + Session.get().getSessionId(), new Callback<JsonArray>() {
            @Override
            public void success(JsonArray appKiuwans, Response response) {
                TypedInput body = response.getBody();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                    StringBuilder out = new StringBuilder();
                    String newLine = System.getProperty("line.separator");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                        out.append(newLine);
                    }

                    // Prints the correct String representation of body.
                    Log.d("RETROFIT response", String.valueOf(out));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recyclerAdapter.clearData();
                recyclerAdapter = new RecyclerViewAdapter(appKiuwans);

                Log.d("jsonarray response", appKiuwans.toString());
                recyclerView.setAdapter(recyclerAdapter);
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("AppsListError", "FAILURE");
                Log.d("AppsListError", error.toString());

                Log.d("Tipo de error", error.getKind().toString());
            }
        });
    }


    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

    }


    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private JsonArray jsonArray;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextViewName;
            public TextView mTextViewDescription;
            public View mView;
            public CardView mCardView;
            public ViewHolder(View v) {
                super(v);
                mTextViewName = (TextView) v.findViewById(R.id.txtApp);
                mTextViewDescription = (TextView) v.findViewById(R.id.txtAppDescription);
                mView = v;
                mCardView = (CardView) v.findViewById(R.id.card_viewRV);
            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public RecyclerViewAdapter(JsonArray jso) {
            jsonArray = jso;
          //  mDataset = myDataset;
        }

        public void clearData(JsonArray jso) {
            jsonArray = jso;
            //  mDataset = myDataset;
        }

        // Clean all elements of the recycler


        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextViewName.setText(jsonArray.get(position).getAsJsonObject().get("name").getAsString());
            holder.mTextViewDescription.setText(jsonArray.get(position).getAsJsonObject().get("description").getAsString());
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String string = "JSESSIONID=" + Session.get().getSessionId();

                    RestClient.apiService.getAppAnalysisCodes(Session.get().getAuthorization(), (String) holder.mTextViewName.getText(), new Callback<List<AppKiuwan>>() {

                        @Override
                        public void success(List<AppKiuwan> codes, Response response) {

                            TypedInput body = response.getBody();
                            try {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                                StringBuilder out = new StringBuilder();
                                String newLine = System.getProperty("line.separator");
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    out.append(line);
                                    out.append(newLine);
                                }

                                // Prints the correct String representation of body.
                                Log.d("RETROFIT response", String.valueOf(out));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("jsonarray response", codes.toString());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("AppsListError", "FAILURE");
                            Log.d("AppsListError", error.toString());

                            Log.d("Tipo de error", error.getKind().toString());
                        }
                    });

                    Context context = v.getContext();
                    Intent intent = new Intent(context, AppDetailedActivity.class);
                    //intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
                    context.startActivity(intent);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return jsonArray.size();

        }

        public void clearData() {
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonArray.remove(i);
            }
            notifyDataSetChanged();
        }

    }



}
