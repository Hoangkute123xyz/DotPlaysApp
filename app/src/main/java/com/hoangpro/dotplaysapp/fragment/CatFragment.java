package com.hoangpro.dotplaysapp.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.adapter.CatVerAdapter;
import com.hoangpro.dotplaysapp.base.BaseFragment;
import com.hoangpro.dotplaysapp.model.Cat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CatFragment extends BaseFragment {

    @Override
    public int setLayout() {
        return R.layout.fragment_cat;
    }

    List<Cat> list;
    CatVerAdapter adapter;
    RecyclerView rvCat;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setTitle(activity.getString(R.string.app_name));
        setData();
    }

    private void setData() {
        list = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dotplays.com/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Document document = Jsoup.parse(s);
                        if (document != null) {
                            Element parents = document.getElementsByClass("widget widget_categories").first();
                            Elements elements = parents.getElementsByTag("a");
                            list = new ArrayList<>();
                            for (Element element : elements) {
                                if (element != null) {
                                    list.add(new Cat(element.text(), element.attr("href")));
                                }
                            }
                            adapter = new CatVerAdapter(list, getActivity());
                            rvCat.setAdapter(adapter);
                            rvCat.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void initView(View view) {
        rvCat = view.findViewById(R.id.rvCats);
    }
}
