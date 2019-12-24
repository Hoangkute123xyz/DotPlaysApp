package com.hoangpro.dotplaysapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.fragment.ViewCatFragment;
import com.hoangpro.dotplaysapp.model.Cat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.hoangpro.dotplaysapp.morefunc.Session.currentCat;

class CatVerHolder extends RecyclerView.ViewHolder {

    ImageView imgThumb;
    TextView tvName;


    public CatVerHolder(@NonNull View itemView) {
        super(itemView);
        imgThumb = itemView.findViewById(R.id.imgThumb);
        tvName = itemView.findViewById(R.id.tvName);
    }
}

public class CatVerAdapter extends RecyclerView.Adapter<CatVerHolder> {
    List<Cat> list;
    Context context;

    public CatVerAdapter(List<Cat> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CatVerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatVerHolder(LayoutInflater.from(context).inflate(R.layout.item_cat_vertical, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatVerHolder holder, int position) {
        final Cat cat = list.get(position);
        holder.tvName.setText(cat.name);
        setThumb(holder.imgThumb,cat.link);
        final AppCompatActivity activity = (AppCompatActivity) context;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCat = cat;
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragView, new ViewCatFragment()).commit();
            }
        });
    }

    private void setThumb(final ImageView imgThumb, String link) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Document document = Jsoup.parse(s);
                        if (document != null) {
                            Element element = document.getElementsByClass("blog-featured-thumbnail").first();
                            if (element != null) {
                                String img = element.attr("style");
                                img = img.replace("background-image:url(", "").replace("(", "");
                                Glide.with(context).load(img).into(imgThumb);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
