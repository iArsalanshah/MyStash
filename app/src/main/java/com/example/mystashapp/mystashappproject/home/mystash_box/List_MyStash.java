package com.example.mystashapp.mystashappproject.home.mystash_box;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mystashapp.mystashappproject.Constant_util;
import com.example.mystashapp.mystashappproject.R;
import com.example.mystashapp.mystashappproject.home.mystash_box.recyclerview_util_mystashlist.RecyclerAdapter_MyStashList;
import com.example.mystashapp.mystashappproject.pojo.get_my_stash_list.GetMyStash;
import com.example.mystashapp.mystashappproject.pojo.get_my_stash_list.Stashlist;
import com.example.mystashapp.mystashappproject.pojo.pojo_login.Users;
import com.example.mystashapp.mystashappproject.webservicefactory.CustomSharedPrefLogin;
import com.example.mystashapp.mystashappproject.webservicefactory.WebServicesFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_MyStash extends AppCompatActivity {
    RecyclerView mRecyclerView;
    TextView altText;
    private RecyclerAdapter_MyStashList mAdapter;
    private ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__mystash);

        altText = (TextView) findViewById(R.id.list_mystash_altText);
        //Progress Dialog
        prog = new ProgressDialog(this);
        prog.show();

        //RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_myStashList);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Retrofit Callback
        getMyStash();
    }

    private void getMyStash() {
        Users cid = CustomSharedPrefLogin.getUserObject(List_MyStash.this);
        Call<GetMyStash> call = WebServicesFactory.getInstance().getMyStashList(Constant_util.ACTION_GET_MYSTASH_LIST, cid.getId());
        call.enqueue(new Callback<GetMyStash>() {
            @Override
            public void onResponse(Call<GetMyStash> call, Response<GetMyStash> response) {
                try {
                    GetMyStash businessResponse = response.body();
                    if (businessResponse.getHeader().getSuccess().equals("1")) {
                        ArrayList<Stashlist> arrSearchBusiness = new
                                ArrayList<>(businessResponse.getBody().getStashlist());
                        mAdapter = new RecyclerAdapter_MyStashList(List_MyStash.this, arrSearchBusiness);
                        prog.dismiss();
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    } else if (businessResponse.getHeader().getSuccess().equals("0")) {
                        prog.dismiss();
                        mRecyclerView.setVisibility(View.GONE);
                        altText.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetMyStash> call, Throwable t) {
                //lv.setVisibility(View.GONE);
                Toast.makeText(List_MyStash.this, "Can't connect to Internet", Toast.LENGTH_SHORT).show();
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
