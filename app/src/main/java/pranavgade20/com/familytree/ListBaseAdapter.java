package pranavgade20.com.familytree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListBaseAdapter extends BaseAdapter {

    private ArrayList<ListDetails> listDetailsArrayList;
    private LayoutInflater inflater;
    private Context context;

    public ListBaseAdapter(Context context, ArrayList<ListDetails> results) {
        listDetailsArrayList = results;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listDetailsArrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        return listDetailsArrayList.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.textView_relation_name);
            holder.relation = (TextView) view.findViewById(R.id.textView_relation);
            holder.age = (TextView) view.findViewById(R.id.textView_relation_age);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set any name you want here
        holder.name.setText(listDetailsArrayList.get(pos).getName());
        // Set any email you want here
        holder.relation.setText(listDetailsArrayList.get(pos).getRelation());
        //
        holder.age.setText(listDetailsArrayList.get(pos).getAge());

        return view;
    }

    static class ViewHolder {
        public TextView name;
        public TextView relation;
        public TextView age;
    }
}

//TODO : change this to add details in the list