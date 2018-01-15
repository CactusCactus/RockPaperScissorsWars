package com.jakub.rockpaperscissorswars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakub.rockpaperscissorswars.adapters.RankingAdapter;
import com.jakub.rockpaperscissorswars.constants.AppConstants;
import com.jakub.rockpaperscissorswars.models.User;
import com.jakub.rockpaperscissorswars.widgets.LoadingScreen;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankingActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.root_layout)
    RelativeLayout relativeLayout;

    private LoadingScreen loadingScreen;
    private RankingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        ButterKnife.bind(this);
        loadingScreen = LoadingScreen.create(this);
        relativeLayout.addView(loadingScreen);
        adapter = new RankingAdapter(Collections.<User>emptyList(), this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child(AppConstants.DB_USERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    users.add(user);
                }
                adapter.setUsers(sortByVictories(users));
                adapter.notifyDataSetChanged();
                relativeLayout.removeView(loadingScreen);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                relativeLayout.removeView(loadingScreen);
            }
        });
    }
    private List<User> sortByVictories(final List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user2.getVictories() - user1.getVictories();
            }
        });
        return users;
    }
}
