package com.example.rachanasolanki.facebookdemo;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListActivity extends AppCompatActivity {

    private ArrayList<FriendsModel> friendList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewFriendListAdapter friendListAdapter;
    private RecyclerViewLoadMoreScroll scrollListener;
    String next,id,name,picURL ;
    public FriendsModel friendsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Bundle b = getIntent().getExtras();

        if(b != null) {
            friendList = b.getParcelableArrayList("friend_list");
            next = b.getString("next");
        }



        Log.e("Friendlist111111111",""+friendList.size());
        Log.e("next",""+next);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        friendListAdapter = new NewFriendListAdapter(FriendListActivity.this,friendList);
        recyclerView.setAdapter(friendListAdapter);

        scrollListener = new RecyclerViewLoadMoreScroll(linearLayoutManager);

        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                friendListAdapter.addLoadingView();
                // int index = arrayPastData.size();
                //  int end = index + 1;

                Log.e("Scrolltest","Scrolltest");
                if (next != null) {
                    getFBFriendsList(next);
                } else {
                    loadFriendsList();
                }

            }
        });

        recyclerView.addOnScrollListener(scrollListener);


    }

    private void parseResponse(JSONObject friends ) {
        List<FriendsModel> dataViews = new ArrayList<>();

        try {
            JSONArray friendsArray = (JSONArray) friends.get("data");

            // facebook use paging if have "next" this mean you still have friends if not start load fbFriends list

            JSONObject paging = friends.getJSONObject("paging");
            Log.e("paging", String.valueOf(paging));

            next = paging.getString("next");

           /* Log.e("next",next);
            if (next != null) {
                getFBFriendsList(next);
            } else {
                loadFriendsList();
            }*/


            if (friendsArray != null) {
                for (int i = 0; i < friendsArray.length(); i++) {

                    try {
                        id = friendsArray.getJSONObject(i).get("id") + "";

                        name = friendsArray.getJSONObject(i).get("name") + "";

                        Log.e("name++",name);
                        JSONObject picObject = new JSONObject(friendsArray.getJSONObject(i).get("picture") + "");
                        picURL = (String) (new JSONObject(picObject.get("data").toString())).get("url");
                        Log.e("imagessssss",picURL);
                        /*item.setPictureURL(picURL);
                        friendsList.add(item);*/

                        friendsModel = new FriendsModel(name,picURL);
                        friendsModel.setName(name);
                        friendsModel.setProfile_pic(picURL);
                        dataViews.add(friendsModel);
                        friendListAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        } catch (JSONException e1) {
            loadFriendsList();
            e1.printStackTrace();
        }

        friendListAdapter.removeLoadingView();
        friendListAdapter.addData(dataViews);
        friendListAdapter.notifyDataSetChanged();
        scrollListener.setLoaded();

    }

    private void getFBFriendsList(String next) {
        //here i used volley to get next page
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, next,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject friends = null;
                        try {
                            friends = new JSONObject(response);
                            parseResponse(friends);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(sr);
    }


    private void loadFriendsList() {
        // swipeLayout.setRefreshing(false);
        if ((friendList != null) && (friendList.size() > 0)) {

            Log.e("friendsList","friendsList");
           /* lvFriendsList.setVisibility(View.VISIBLE);
            friendsAdapter.notifyDataSetChanged();*/
        } else {
            //lvFriendsList.setVisibility(View.GONE);
        }
    }
}
