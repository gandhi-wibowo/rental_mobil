package com.example.gandhi.rental.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gandhi.rental.R;
import com.example.gandhi.rental.model.Testimonis;

import java.util.List;

public class CustomListTestimoni extends BaseAdapter {
	// nampilin testimoni
	private Activity activity;
	private LayoutInflater inflater;
	private List<Testimonis> testimonisItems;

	public CustomListTestimoni(Activity activity, List<Testimonis> testimonisItems) {
		this.activity = activity;
		this.testimonisItems = testimonisItems;
	}

	@Override
	public int getCount() {
		return testimonisItems.size();
	}

	@Override
	public Object getItem(int location) {
		return testimonisItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) convertView = inflater.inflate(R.layout.list_testimoni, null);
		TextView namaTestimoni = (TextView) convertView.findViewById(R.id.namaPenulis);
		TextView tanggalTestimoni = (TextView) convertView.findViewById(R.id.tglPenulis);
		TextView isiTestimoni = (TextView) convertView.findViewById(R.id.isiPenulis);

		Testimonis t = testimonisItems.get(position);

		namaTestimoni.setText(t.getNamaTestimoni());
		tanggalTestimoni.setText(t.getTglTestimoni());
		isiTestimoni.setText(t.getIsiTestimoni());
		return convertView;
	}


}