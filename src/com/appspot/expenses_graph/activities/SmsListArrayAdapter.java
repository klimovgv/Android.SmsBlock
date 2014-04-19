package com.appspot.expenses_graph.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.appspot.expenses_graph.R;
import com.appspot.expenses_graph.models.Sms;

import java.util.List;

public class SmsListArrayAdapter extends ArrayAdapter<Sms> {
    private final Context context;
    private final List<Sms> smsList;

    public SmsListArrayAdapter(Context context, List<Sms> smsList) {
        super(context, R.layout.sms_row, smsList);

        this.context = context;
        this.smsList = smsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.sms_row, parent, false);

        TextView messageTextView = (TextView) rowView.findViewById(R.id.message_text);
        messageTextView.setText(smsList.get(position).getMessageText());

        TextView phoneNumberView = (TextView) rowView.findViewById(R.id.phone_number);
        phoneNumberView.setText(smsList.get(position).getPhoneNumber());

        TextView timestampView = (TextView) rowView.findViewById(R.id.timestamp);
        timestampView.setText(smsList.get(position).getTimestamp());

        return rowView;
    }
}
