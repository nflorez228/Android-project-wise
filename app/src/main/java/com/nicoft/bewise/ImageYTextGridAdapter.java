package com.nicoft.bewise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nicolas on 08/10/2015.
 */
public class ImageYTextGridAdapter extends BaseAdapter{

    Context c;
    ArrayList<CampoDeGridView> campo;

    public ImageYTextGridAdapter(Context c,ArrayList<CampoDeGridView> campo)
    {
        this.c=c;
        this.campo=campo;
    }

    @Override
    public int getCount() {
        return campo.size();
    }

    @Override
    public Object getItem(int position) {
        return campo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView= inflater.inflate(R.layout.campo_grid_view,parent,false);
        }

        ImageView imageH=(ImageView)convertView.findViewById(R.id.imageViewHouseCampoGridView);
        ImageView imageW=(ImageView)convertView.findViewById(R.id.imageViewWorkCampoGridView);

        TextView text=(TextView)convertView.findViewById(R.id.textViewCampoGridView);

        if(campo.get(position).getTipo().equals("Office")) {
            imageW.setImageResource(campo.get(position).getImage());
            imageW.setVisibility(View.VISIBLE);
        }
        else {
            imageH.setImageResource(campo.get(position).getImage());
            imageH.setVisibility(View.VISIBLE);
        }


        text.setText(campo.get(position).getTitle());

        return convertView;
    }
}
