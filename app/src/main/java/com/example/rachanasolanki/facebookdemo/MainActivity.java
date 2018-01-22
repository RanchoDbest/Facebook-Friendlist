package com.example.rachanasolanki.facebookdemo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.service.textservice.SpellCheckerService;
import android.support.constraint.solver.Goal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
    String id,name,picURL;
    public ArrayList<FriendsModel> friendsList;
    public FriendsModel friendsModel;
    Button btn_friendlist;
    public String next;
    FancyButton fancy_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friendsList = new ArrayList<>();
        btn_friendlist = (Button) findViewById(R.id.btn_friendlist);
        btn_friendlist.setVisibility(View.GONE);
        fancy_btn = (FancyButton) findViewById(R.id.fancy_btn);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        //loginButton.setFragment(MainActivity.this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        btn_friendlist.setVisibility(View.VISIBLE);

                        final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                if(response != null){

                                    try {

                                        Log.e("response11",""+response.getJSONObject());

                                        JSONObject taggable_friends = response.getJSONObject().getJSONObject("taggable_friends");
                                        Log.e("taggable_friends1",""+taggable_friends);


                                        JSONArray friendsArray = (JSONArray) taggable_friends.getJSONArray("data");

                                        // facebook use paging if have "next" this mean you still have friends if not start load fbFriends list

                                        JSONObject paging = taggable_friends.getJSONObject("paging");
                                        Log.e("paging", String.valueOf(paging));

                                         next = paging.getString("next");

                                        Log.e("next",next);

                                        //when we want to call same api in same activity

                                        /*if (next != null) {
                                            getFBFriendsList(next);
                                        } else {
                                            loadFriendsList();
                                        }*/


                                        if (friendsArray != null) {
                                            for (int i = 0; i < friendsArray.length(); i++) {

                                                try {
                                                    id = friendsArray.getJSONObject(i).get("id") + "";

                                                    name = friendsArray.getJSONObject(i).get("name") + "";

                                                    JSONObject picObject = new JSONObject(friendsArray.getJSONObject(i).get("picture") + "");
                                                    picURL = (String) (new JSONObject(picObject.get("data").toString())).get("url");

                                                    friendsModel = new FriendsModel(name,picURL);
                                                    friendsModel.setName(name);
                                                    friendsList.add(friendsModel);



                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Log.e("Arraylist",""+friendsList.size());
                                            }

                                        }
                                    } catch (JSONException e1) {
                                        loadFriendsList();
                                        e1.printStackTrace();
                                    }

                                }

                            }
                        });

                        final Bundle parameters = new Bundle();
                        //parameters.putString("fields", "name,email,id,user_friends");
                        parameters.putString("fields", "email,taggable_friends");

                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code

                    }
                });
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("read_custom_friendlists"));


        fancy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null){
                    LoginManager.getInstance().logOut();
                    btn_friendlist.setVisibility(View.GONE);
                    Log.e("logout","logout");
                    fancy_btn.setText("Facebook Login");
                } else {
                    //mAccessTokenTracker.startTracking();
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("read_custom_friendlists"));
                    btn_friendlist.setVisibility(View.VISIBLE);
                    Log.e("login","login");
                    fancy_btn.setText("Logout");

                }
            }
        });



        btn_friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("friend_list",""+friendsList.size());
                Intent i = new Intent(MainActivity.this,FriendListActivity.class);
                i.putExtra("friend_list", friendsList);
                i.putExtra("next",next);
                startActivity(i);
            }
        });



    }

    private void parseResponse(JSONObject friends ) {

        try {
            JSONArray friendsArray = (JSONArray) friends.get("data");

            // facebook use paging if have "next" this mean you still have friends if not start load fbFriends list

            JSONObject paging = friends.getJSONObject("paging");
            Log.e("paging", String.valueOf(paging));

            next = paging.getString("next");

            Log.e("next",next);
            if (next != null) {
                getFBFriendsList(next);
            } else {
                loadFriendsList();
            }


            if (friendsArray != null) {
                for (int i = 0; i < friendsArray.length(); i++) {

                    try {
                        id = friendsArray.getJSONObject(i).get("id") + "";

                        name = friendsArray.getJSONObject(i).get("name") + "";

                        Log.e("name++",name);
                        /*JSONObject picObject = new JSONObject(friendsArray
                                .getJSONObject(i).get("picture") + "");
                        String picURL = (String) (new JSONObject(picObject
                                .get("data").toString())).get("url");
                        item.setPictureURL(picURL);
                        friendsList.add(item);*/

                        friendsModel = new FriendsModel(name,picURL);
                        friendsModel.setName(name);
                        friendsModel.setProfile_pic(picURL);
                        friendsList.add(friendsModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        } catch (JSONException e1) {
            loadFriendsList();
            e1.printStackTrace();
        }


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
                          //  parseResponse(friends);
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
        if ((friendsList != null) && (friendsList.size() > 0)) {

            Log.e("friendsList","friendsList");
           /* lvFriendsList.setVisibility(View.VISIBLE);
            friendsAdapter.notifyDataSetChanged();*/
        } else {
            //lvFriendsList.setVisibility(View.GONE);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
