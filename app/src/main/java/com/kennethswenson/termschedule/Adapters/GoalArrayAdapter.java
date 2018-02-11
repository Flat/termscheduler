package com.kennethswenson.termschedule.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kennethswenson.termschedule.Models.Goal;
import com.kennethswenson.termschedule.R;
import com.kennethswenson.termschedule.utils.Formatting;

import java.util.List;

public class GoalArrayAdapter extends ArrayAdapter<Goal> {
    private Context context;
    private int resource;
    private List<Goal> goals;

    public GoalArrayAdapter(@NonNull Context context, int resource, @NonNull List<Goal> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.goals = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        GoalHolder goalHolder = null;
        if (row == null){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            row = layoutInflater.inflate(resource, parent, false);
            goalHolder = new GoalHolder();
            goalHolder.title = row.findViewById(R.id.title);
            goalHolder.date = row.findViewById(R.id.date);
            row.setTag(goalHolder);
        } else {
            goalHolder = (GoalHolder)row.getTag();
        }

        Goal goal = goals.get(position);
        goalHolder.title.setText(goal.getTitle());
        goalHolder.date.setText(goal.getGoalDateTime().format(Formatting.getDateTimeFormat()));

        return row;

    }

    static class GoalHolder{
        TextView title;
        TextView date;
    }
}
