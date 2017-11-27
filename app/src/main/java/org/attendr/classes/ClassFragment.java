package org.attendr.classes;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        details.setPercentage(55);
        cardAdapter.addClass(details);
        ClassDetails details2 = new ClassDetails();
        details2.setProfessor("Joe Guilliams");
        details2.setClassTitle("CS 2050");
        details2.setPercentage(96);
        cardAdapter.addClass(details2);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return rootView;
    }

    private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        Drawable background;
        Drawable trashDrawable;
        boolean initiated;

        private void init() {
            background = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorDeleteRed));
            trashDrawable = ContextCompat.getDrawable(getContext(), R.drawable.trashcan);
            initiated = true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float originDX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            if (viewHolder.getAdapterPosition() == -1) {
                return;
            }

            if (!initiated) {
                init();
            }

            int dX = (int) originDX;

            int itemHeight = itemView.getBottom() - itemView.getTop();

            if (-dX >= itemHeight) {
                dX = -itemHeight;
            } else if(dX > 0) {
                dX = 0;
            }

            int backgroundLeft = itemView.getRight() + dX;
            int backgroundRight = itemView.getRight();

            background.setBounds(backgroundLeft, itemView.getTop(), backgroundRight, itemView.getBottom());
            background.draw(c);

            int intrinsicWidth = trashDrawable.getIntrinsicWidth();
            int intrinsicHeight = trashDrawable.getIntrinsicWidth();

            int canLeft = itemView.getRight() - (itemHeight/2) - (intrinsicWidth/2);
            int canRight = itemView.getRight() - (itemHeight/2) + (intrinsicWidth/2);
            int canTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
            int canBottom = canTop + intrinsicHeight;
            trashDrawable.setBounds(canLeft, canTop, canRight, canBottom);

            trashDrawable.draw(c);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

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
            percentageWheel.setProgress(details.getPercentage());
            classTitle.setText(details.getClassTitle());
        }
    }
}
