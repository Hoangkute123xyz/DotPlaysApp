package com.hoangpro.dotplaysapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.activity.MainActivity;
import com.hoangpro.dotplaysapp.adapter.PostListAdapter;
import com.hoangpro.dotplaysapp.base.BaseFragment;
import com.hoangpro.dotplaysapp.model.Post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";

    @Override
    public int setLayout() {
        return R.layout.fragment_home;
    }

    RecyclerView rvPost;
    PostListAdapter adapter;
    ProgressBar pgLoadMore;
    List<Post> list;
    private final String URL = "http://www.dotplays.com/";
    private int currentPage = 1;
    private boolean isNext = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intView(view);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setTitle(activity.getString(R.string.app_name));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading, null, false));
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        dialog = alertDialog;
        alertDialog.show();
        getData();

    }

    private Dialog dialog;

    private void getData() {
        list = new ArrayList<>();
        adapter = new PostListAdapter(list, getActivity(),true);
        rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition()==list.size()-1 && isNext){
                    isNext=false;
                    Log.e(TAG, String.valueOf(currentPage));
                    pgLoadMore.setVisibility(View.VISIBLE);
                    getPagePost();
                }
            }
        });
        rvPost.setAdapter(adapter);
        rvPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Document document = Jsoup.parse(s);
                if (document != null) {
                    Elements elements = document.select("div.post-blogs-container-thumbnails");
                    for (Element element : elements) {
                        Element elmName = element.getElementsByTag("a").last();
                        Element elmImg = element.getElementsByClass("blog-featured-thumbnail").first();
                        Element elmTime = element.getElementsByClass("entry-meta").first();
                        Element elmDescription = element.getElementsByClass("post-content").first();

                        if (elmName != null && elmImg != null && elmTime != null && elmDescription != null) {
                            String img = elmImg.attr("style");
                            img = img.replace("background-image:url(", "").replace(")", "");
                            list.add(new Post(elmName.text(), img, elmTime.text(), elmName.attr("href"), elmDescription.text()));
                        }
                    }
                    isNext=document.getElementsByClass("next page-numbers").size()>0;
                    adapter.notifyDataSetChanged();
                    currentPage++;
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    private void getPagePost() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "page/" + currentPage, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Document document = Jsoup.parse(s);
                if (document != null) {
                    Elements elements = document.select("div.post-blogs-container-thumbnails");
                    for (Element element : elements) {
                        Element elmName = element.getElementsByTag("a").last();
                        Element elmImg = element.getElementsByClass("blog-featured-thumbnail").first();
                        Element elmTime = element.getElementsByClass("entry-meta").first();
                        Element elmDescription = element.getElementsByClass("post-content").first();
                        if (elmName != null && elmImg != null && elmTime != null && elmDescription != null) {
                            String img = elmImg.attr("style");
                            img = img.replace("background-image:url(", "").replace(")", "");
                            list.add(new Post(elmName.text(), img, elmTime.text(), elmName.attr("href"), elmDescription.text()));
                        }

                    }
                    isNext=document.getElementsByClass("next page-numbers").size()>0;
                    adapter.notifyDataSetChanged();
                    currentPage++;
                    pgLoadMore.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void intView(View view) {
        rvPost = view.findViewById(R.id.rvPost);
        pgLoadMore = view.findViewById(R.id.pgLoadMore);
    }
}
