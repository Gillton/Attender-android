package org.attendr.classes;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.attendr.R;
import org.attendr.classes.models.ClassDetails;
import org.attendr.helpers.views.PercentageCircle;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.UP;

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
        details.setPercentage(55);
        cardAdapter.addClass(details);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof CardHolder) {
                    CardHolder holder = (CardHolder) viewHolder;
                    if (direction == ItemTouchHelper.LEFT) {
                        holder.moveToLeft();
                    } else {
                        holder.moveToRight();
                    }

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
            }
        };

// attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

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

        private RelativeLayout foreground;

        private PercentageCircle percentageWheel;
        private TextView classTitle;

        private boolean leftMoved = false;
        private boolean rightMoved = false;

        private int dp;

        CardHolder(View itemView) {
            super(itemView);
            foreground = itemView.findViewById(R.id.foreground);
            percentageWheel = itemView.findViewById(R.id.percentageWheel);
            classTitle = itemView.findViewById(R.id.classTitle);
            dp = (int) (100 * Resources.getSystem().getDisplayMetrics().density);
        }

        void moveToLeft() {
            if (!leftMoved) {
                leftMoved = true;
                rightMoved = false;
                foreground.offsetLeftAndRight(-dp);
            }
        }

        void moveToRight() {
            if (!rightMoved) {
                leftMoved = false;
                rightMoved = true;
                foreground.offsetLeftAndRight(dp);
            }
        }

        void setData(ClassDetails details) {
            percentageWheel.setProgress(details.getPercentage());
            classTitle.setText(details.getClassTitle());
        }
    }
}
