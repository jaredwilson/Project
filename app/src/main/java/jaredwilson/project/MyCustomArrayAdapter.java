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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyCustomArrayAdapter extends ArrayAdapter<File>{
    public MyCustomArrayAdapter(Context context, File[] filenames, File filePath) {
        super(context, -1, filenames);
        this.context = context;
        this.filenames = new ArrayList<>(Arrays.asList(filenames[0].getParentFile().list()));
        this.lengths = new ArrayList<>();
        this.modifieds = new ArrayList<>();
        for (File fn:
             filenames) {
            lengths.add((MediaPlayer.create(context, Uri.parse(fn.getPath()))).getDuration());
            modifieds.add(fn.lastModified());
        }
        this.sdf1 = new SimpleDateFormat("mm'm':ss's':SSS'ms'");
        this.sdf2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }

    private final Context context;
    private final List<String> filenames;
    private final List<Integer> lengths;
    private final List<Long> modifieds;
    private final SimpleDateFormat sdf1;
    private final SimpleDateFormat sdf2;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_custom_listview_layout, parent, false);
        TextView tv1  = (TextView)rowView.findViewById(R.id.firstLine);
        TextView tv2  = (TextView)rowView.findViewById(R.id.lengthTextView);
        TextView tv3  = (TextView)rowView.findViewById(R.id.modifiedTextView);

        tv1.setText(filenames.get(position));
        tv2.setText(sdf1.format(lengths.get(position)));
        tv3.setText(sdf2.format(modifieds.get(position)));

        return rowView;
    }

}

/*
    public static void main(String[] args)
    {
	File file = new File("c:\\logfile.log");

	System.out.println("Before Format : " + file.lastModified());

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	System.out.println("After Format : " + sdf.format(file.lastModified()));
    }

    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    return (new SimpleDateFormat("mm:ss:SSS")).format(new Date(time));

 */