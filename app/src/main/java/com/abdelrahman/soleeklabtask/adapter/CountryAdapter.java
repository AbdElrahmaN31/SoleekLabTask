package com.abdelrahman.soleeklabtask.adapter;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdelrahman.soleeklabtask.R;
import com.abdelrahman.soleeklabtask.model.Country;
import com.abdelrahman.soleeklabtask.model.Language;
import com.abdelrahman.soleeklabtask.utils.SvgSoftwareLayerSetter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<Country> countriesList;
    private Context mContext;
    private RequestBuilder<PictureDrawable> requestBuilder;

    public CountryAdapter(List<Country> countriesList, Context context) {
        this.countriesList = countriesList;
        this.mContext = context;
        requestBuilder = Glide.with(mContext)
                .as(PictureDrawable.class)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item_list, parent, false);

        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CountryViewHolder holder, int position) {
        Country country = countriesList.get(position);
        holder.name.setText(country.getName());
        holder.capital.setText("Capital: " + country.getCapital());
        String languages = "";
        for (Language language : country.getLanguages())
            languages += language.getName() + " ";

        holder.language.setText("Languages: " + languages);

        requestBuilder
                .load(country.getFlag())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.flag);
    }

    @Override
    public int getItemCount() {
        return countriesList.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {
        public TextView name, language, capital;
        ImageView flag;


        public CountryViewHolder(View view) {
            super(view);
            flag = view.findViewById(R.id.image_country_flag);
            name = view.findViewById(R.id.text_country_name);
            capital = view.findViewById(R.id.text_country_capital);
            language = view.findViewById(R.id.text_country_languages);
        }
    }
}