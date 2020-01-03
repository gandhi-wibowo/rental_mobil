package com.example.gandhi.rental.adater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.gandhi.rental.R;
import com.example.gandhi.rental.app.AppController;
import com.example.gandhi.rental.model.Mobil;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
	// untuk nampilin mobil di main menu
	private Activity activity;
	private LayoutInflater inflater;
	private List<Mobil> mobilItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<Mobil> mobilItems) {
		this.activity = activity;
		this.mobilItems = mobilItems;
	}

	@Override
	public int getCount() {
		return mobilItems.size();
	}

	@Override
	public Object getItem(int location) {
		return mobilItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null) inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) convertView = inflater.inflate(R.layout.list_row, null);
		if (imageLoader == null) imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
		TextView nmMobil = (TextView) convertView.findViewById(R.id.namaMobil);
		TextView wrMobil = (TextView) convertView.findViewById(R.id.warnaMobil);
		TextView hrgMobil = (TextView) convertView.findViewById(R.id.hargaSewa);
		TextView stsMobil = (TextView) convertView.findViewById(R.id.statusMobil);

		// getting movie data for the row
		Mobil m = mobilItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		
		// nama mobil
		nmMobil.setText(m.getNmMobil());
		// warna mobil
		wrMobil.setText(m.getWrMobil());
		// harga
		hrgMobil.setText(m.getHrgMobil());
		// status mobil
		stsMobil.setText(m.getStsMobil());

		return convertView;
	}


}