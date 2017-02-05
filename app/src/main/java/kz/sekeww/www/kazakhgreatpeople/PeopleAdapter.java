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
 * Created by BEK on 05.02.2017.
 */
public class PeopleAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<People> peoples;
    private Context context;

    public PeopleAdapter(Context context, List<People> peoples) {
        this.context = context;
        this.peoples = peoples;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return peoples.size();
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

         if (convertView == null) {
             convertView = inflater.inflate(R.layout.row_people_item, null);
             viewHolder = new ViewHolder(convertView);
             convertView.setTag(viewHolder);
         } else {
             viewHolder = (ViewHolder) convertView.getTag();
         }

        viewHolder.peopleTitleTextView.setText(peoples.get(position).getTitle());
        viewHolder.peopleAgeTextView.setText(peoples.get(position).getAge());

        Glide.with(context).load(peoples.get(position).getImage()).centerCrop().into(viewHolder.peopleImageView);

        return convertView;
    }

    private class ViewHolder {

        ImageView peopleImageView;
        TextView peopleTitleTextView;
        TextView peopleAgeTextView;

        public ViewHolder(View v) {
            peopleImageView = (ImageView) v.findViewById(R.id.peopleImageView);
            peopleAgeTextView = (TextView) v.findViewById(R.id.peopleAgeTextView);
            peopleTitleTextView = (TextView) v.findViewById(R.id.peopleTitleTextView);
        }
    }
}
