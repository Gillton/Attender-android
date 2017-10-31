package org.attendr.classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.attendr.R;

public class ClassFragment extends Fragment {

	private RecyclerView recyclerView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_classes, container, false);
		recyclerView = rootView.findViewById(R.id.recyclerView);
		CardAdapter cardAdapter = new CardAdapter();
		recyclerView.setAdapter(cardAdapter);
		LinearLayoutManager manager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(manager);

		return rootView;
	}

	class CardAdapter extends RecyclerView.Adapter<CardHolder> {



		@Override
		public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return null;
		}

		@Override
		public void onBindViewHolder(CardHolder holder, int position) {

		}

		@Override
		public int getItemCount() {
			return 0;
		}
	}

	class CardHolder extends RecyclerView.ViewHolder {

		public CardHolder(View itemView) {
			super(itemView);
		}
	}
}
