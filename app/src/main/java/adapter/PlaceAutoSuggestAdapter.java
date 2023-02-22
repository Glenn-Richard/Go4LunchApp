package adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

import models.Restaurant;

public class PlaceAutoSuggestAdapter extends ArrayAdapter<Restaurant> implements Filterable {

    List<Restaurant> results;

    int resource;
    Context context;

    public PlaceAutoSuggestAdapter(int resource, Context context,List<Restaurant> restaurants){
        super(context,resource);
        this.resource = resource;
        this.context = context;
        this.results = restaurants;
    }

    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public Restaurant getItem(int position){
        return results.get(position);
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence!=null){
                    filterResults.values = results;
                    filterResults.count = results.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(results!=null&&results.size()>0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
