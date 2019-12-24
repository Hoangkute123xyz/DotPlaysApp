package com.hoangpro.dotplaysapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hoangpro.dotplaysapp.R;
import com.hoangpro.dotplaysapp.activity.MainActivity;
import com.hoangpro.dotplaysapp.base.BaseActivity;
import com.hoangpro.dotplaysapp.fragment.ViewCatFragment;
import com.hoangpro.dotplaysapp.model.Cat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.hoangpro.dotplaysapp.morefunc.Session.currentCat;

class CatHzHolder extends RecyclerView.ViewHolder {

    ImageView imgThumb;
    TextView tvName;

    public CatHzHolder(@NonNull View itemView) {
        super(itemView);
        imgThumb = itemView.findViewById(R.id.imgThumb);
        tvName = itemView.findViewById(R.id.tvName);
    }
}

public class CatHzAdapter extends RecyclerView.Adapter<CatHzHolder> {
    List<Cat> list;
    Context context;

    public CatHzAdapter(List<Cat> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CatHzHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatHzHolder(LayoutInflater.from(context).inflate(R.layout.item_cat_hz, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatHzHolder holder, int position) {
        final Cat cat = list.get(position);
        holder.tvName.setText(cat.name);
        getImgThumb(cat.link, holder.imgThumb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCat = cat;
                MainActivity activity = (MainActivity) context;
                activity.bottomNavigationView.setSelectedItemId(R.id.item_categories);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragView, new ViewCatFragment()).commit();
            }
        });
    }

    public void getImgThumb(String url, final ImageView imageView) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Document document = Jsoup.parse(s);
                        if (document != null) {
                            Element element = document.getElementsByClass("blog-featured-thumbnail").first();
                            if (element != null) {
                                String img = element.attr("style");
                                img = img.replace("background-image:url(", "").replace("(", "");
                                Glide.with(context).load(img).into(imageView);
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
