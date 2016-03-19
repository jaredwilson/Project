package jaredwilson.project;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyCustomArrayAdapter extends ArrayAdapter<File>{
    public MyCustomArrayAdapter(Context context, File[] filenames) {
        super(context, -1, filenames);
        this.context = context;
        this.filenames = filenames[0].getParentFile().list();
        this.lengths = new ArrayList<>();
        this.modifieds = new ArrayList<>();
        for (File fn:
             filenames) {
            lengths.add((MediaPlayer.create(context, Uri.parse(fn.getPath()))).getDuration());
            modifieds.add(fn.lastModified());
        }
    }

    private final Context context;
    private final String[] filenames;
    private final List<Integer> lengths;
    private final List<Long> modifieds;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_custom_listview_layout, parent, false);
        TextView tv1  = (TextView)rowView.findViewById(R.id.firstLine);
        TextView tv2  = (TextView)rowView.findViewById(R.id.lengthTextView);
        TextView tv3  = (TextView)rowView.findViewById(R.id.modifiedTextView);

        tv1.setText(filenames[position]);
        tv2.setText(Integer.toString(lengths.get(position)));


        return rowView;
    }

}
