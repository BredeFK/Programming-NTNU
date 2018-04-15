package com.example.bredefk.lab02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapter extends ArrayAdapter<RssFeedModel> {
    private int LIMIT;
    private TextView txtTitle;
    private TextView txtLink;
    private View view;
    private RssFeedModel rssFeedModel;

    public Adapter(Context context, int resource, List<RssFeedModel> items, int limit) {
        super(context, resource, items);
        LIMIT = limit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.rss_items, null);
        }

        rssFeedModel = getItem(position);

        if (rssFeedModel != null) {
            txtTitle = view.findViewById(R.id.txt_title);
            txtLink = view.findViewById(R.id.txt_link);

            if (txtTitle != null) {
                txtTitle.setText(rssFeedModel.title);
            }
            if (txtLink != null) {
                txtLink.setText(rssFeedModel.link);
            }
        }
        return view;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        if (count >= LIMIT) {
            return LIMIT;
        } else {
            return count;
        }
    }
}

