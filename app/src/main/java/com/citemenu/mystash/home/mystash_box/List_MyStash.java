package com.citemenu.mystash.home.mystash_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.citemenu.mystash.helper.Constant_util;
import com.citemenu.mystash.home.mystash_box.recyclerview_util_mystashlist.RecyclerAdapter_MyStashList;
import com.citemenu.mystash.pojo.get_my_stash_list.GetMyStash;
import com.citemenu.mystash.pojo.get_my_stash_list.Stashlist;
import com.citemenu.mystash.pojo.pojo_login.Users;
import com.citemenu.mystash.webservicefactory.CustomSharedPref;
import com.citemenu.mystash.webservicefactory.WebServicesFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_MyStash extends AppCompatActivity {
    RecyclerView mRecyclerView;
    TextView altText;
    private ProgressDialog prog;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__mystash);

        altText = (TextView) findViewById(R.id.list_mystash_altText);
        //Progress Dialog
        prog = new ProgressDialog(this);
        prog.setMessage("Loading...");
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_MyStashList);

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_myStashList);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyStash();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyStash();
    }

    private void getMyStash() {
        prog.show();
        Users cid = CustomSharedPref.getUserObject(List_MyStash.this);
        Call<GetMyStash> call = WebServicesFactory.getInstance().getMyStashList(Constant_util.ACTION_GET_MYSTASH_LIST, cid.getId());
        call.enqueue(new Callback<GetMyStash>() {
            @Override
            public void onResponse(Call<GetMyStash> call, Response<GetMyStash> response) {
                prog.dismiss();
                swipeContainer.setRefreshing(false);
                try {
                    GetMyStash businessResponse = response.body();
                    switch (businessResponse.getHeader().getSuccess()) {
                        case "1":
                            if (businessResponse.getBody().getStashlist().isEmpty() && businessResponse.getBody().getStashlist().size() == 0) {
                                mRecyclerView.setVisibility(View.GONE);
                                altText.setVisibility(View.VISIBLE);
                                Toast.makeText(List_MyStash.this, "" + businessResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                ArrayList<Stashlist> arrSearchBusiness = new
                                        ArrayList<>(businessResponse.getBody().getStashlist());
                                RecyclerAdapter_MyStashList mAdapter = new RecyclerAdapter_MyStashList(List_MyStash.this, arrSearchBusiness);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            break;
                        case "0":
                            mRecyclerView.setVisibility(View.GONE);
                            altText.setVisibility(View.VISIBLE);
                            break;
                        default:
                            Toast.makeText(List_MyStash.this, "" + businessResponse.getHeader().getMessage(), Toast.LENGTH_SHORT).show();
                            mRecyclerView.setVisibility(View.GONE);
                            altText.setVisibility(View.VISIBLE);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetMyStash> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                prog.dismiss();
                Toast.makeText(List_MyStash.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backMyStashRecyclerViewImageBtn(View view) {
        finish();
    }

    public void PlusMyStashRecyclerImageView(View view) {
        SearchBusiness_MyStash.IS_CHECK_IN = false;
        startActivity(new Intent(this, SearchBusiness_MyStash.class));
    }
}
