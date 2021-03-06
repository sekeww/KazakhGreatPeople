package kz.sekeww.www.kazakhgreatpeople;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by Askhat on 6/15/2016.
 */

public class CategoryAdapter extends BaseAdapter {

    private List<Category> categories;
    private Context context;
    private LayoutInflater inflater;


    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_category_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.cityTitleListView.setText(categories.get(position).getTitle());

        //imageLoader.DisplayImage(categories.get(position).getImage(), viewHolder.cityImageView, context);
        Glide.with(context).load(categories.get(position).getImage()).centerCrop().into(viewHolder.cityImageView);

        return convertView;
    }

    private class ViewHolder {
        ImageView cityImageView;
        TextView cityTitleListView;

        public ViewHolder(View convertView){
            cityImageView  = (ImageView) convertView.findViewById(R.id.cityImageView);
            cityTitleListView = (TextView) convertView.findViewById(R.id.cityTitleTextView);

        }
    }
}
