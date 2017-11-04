package org.attendr.classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.attendr.R;
import org.attendr.classes.models.ClassDetails;
import org.attendr.helpers.views.PercentageCircle;

import java.util.ArrayList;
import java.util.List;

public class ClassFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_classes, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        CardAdapter cardAdapter = new CardAdapter();
        recyclerView.setAdapter(cardAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        ClassDetails details = new ClassDetails();
        details.setProfessor("Silvia Bompadre");
        details.setClassTitle("University Physics 1");
        cardAdapter.addClass(details);
        return rootView;
    }

    class CardAdapter extends RecyclerView.Adapter<CardHolder> {

        List<ClassDetails> classes = new ArrayList<>();

        void addClass(ClassDetails details) {
            classes.add(details);
            notifyItemInserted(classes.size() - 1);
        }

        void removeClass(ClassDetails details) {
            int position = classes.indexOf(details);
            if (position != -1) {
                classes.remove(details);
                notifyItemRemoved(position);
            }
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_card, parent, false));
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            if (!classes.isEmpty() && classes.size() > position) {
                holder.setData(classes.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return classes.size();
        }
    }

    class CardHolder extends RecyclerView.ViewHolder {

        private PercentageCircle percentageWheel;
        private TextView classTitle;

        CardHolder(View itemView) {
            super(itemView);
            percentageWheel = itemView.findViewById(R.id.percentageWheel);
            classTitle = itemView.findViewById(R.id.classTitle);
        }

        void setData(ClassDetails details) {
            percentageWheel.setProgress(86);
            classTitle.setText(details.getClassTitle());
        }
    }
}
