package com.example.fisegoogle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
 
public class DashboardActivity extends Activity implements OnItemClickListener {
 
    static final String EXTRA_MAP = "map";
 
    static final LauncherIcon[] ICONS = {
            new LauncherIcon(R.drawable.restaurant, "Рестораны", "restaurant.png"),
            new LauncherIcon(R.drawable.cafe, "Кафе", "cafe.png"),
            new LauncherIcon(R.drawable.bar, "Бары", "bar.png"),
            new LauncherIcon(R.drawable.map, "Карта", "map.png"),
    };
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
 
        GridView gridview = (GridView) findViewById(R.id.dashboard_grid);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(this);
 
        // Hack to disable GridView scrolling
        gridview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE;
            }
        });
    }
 
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this, MainActivity.class);
        Intent intent2 = new Intent(this, MapActivity.class);
        Intent intent3 = new Intent(this, CafeActivity.class);
        Intent intent4 = new Intent(this, BarActivity.class);
        if (position == 0) {
        	intent.putExtra(EXTRA_MAP, ICONS[0].map);
        	startActivity(intent);
        }
        else if (position == 1) {
        	intent3.putExtra(EXTRA_MAP, ICONS[1].map);
        	startActivity(intent3);
        }
        else if (position == 2) { 
        	intent3.putExtra(EXTRA_MAP, ICONS[2].map);
        	startActivity(intent4);
        }
        else if (position == 3) {
        	intent2.putExtra(EXTRA_MAP, ICONS[3].map);
        	startActivity(intent2);
        }
    }
 
    static class LauncherIcon {
        final String text;
        final int imgId;
        final String map;
 
        public LauncherIcon(int imgId, String text, String map) {
            super();
            this.imgId = imgId;
            this.text = text;
            this.map = map;
        }
 
    }
 
    static class ImageAdapter extends BaseAdapter {
        private Context mContext;
 
        public ImageAdapter(Context c) {
            mContext = c;
        }
 
        @Override
        public int getCount() {
            return ICONS.length;
        }
 
        @Override
        public LauncherIcon getItem(int position) {
            return null;
        }
 
        @Override
        public long getItemId(int position) {
            return 0;
        }
 
        static class ViewHolder {
            public ImageView icon;
            public TextView text;
        }
 
        // Create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
 
                v = vi.inflate(R.layout.dashboard_icon, null);
                holder = new ViewHolder();
                holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);
                holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
 
            holder.icon.setImageResource(ICONS[position].imgId);
            holder.text.setText(ICONS[position].text);
 
            return v;
        }
    }
}