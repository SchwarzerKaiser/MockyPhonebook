package com.leewilson.mockyphonebook.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.leewilson.mockyphonebook.R;
import com.leewilson.mockyphonebook.adapters.ContactListAdapter;
import com.leewilson.mockyphonebook.di.AppComponent;
import com.leewilson.mockyphonebook.di.DaggerAppComponent;
import com.leewilson.mockyphonebook.model.Contact;
import com.leewilson.mockyphonebook.util.SortConfig;
import com.leewilson.mockyphonebook.viewmodels.MainViewModelFactory;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Inject
    MainViewModelFactory mFactory;

    private MainViewModel mViewModel;
    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ContactListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppComponent component = DaggerAppComponent.create();
        component.inject(this);

        mViewModel = new ViewModelProvider(this, mFactory)
                .get(MainViewModel.class);

        initViews();
        initRecyclerView();
        subscribeObservers();
        initSearchView();
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                mAdapter.getFilter().filter(text);
                return false;
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new ContactListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        mLinearLayout = findViewById(R.id.linearLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchView = findViewById(R.id.searchView);
    }

    private void subscribeObservers() {
        mViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if (contacts != null) {
                    mAdapter.submitList(contacts);
                }
            }
        });

        mViewModel.getErrorFlag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean networkErrorHasOccurred) {
                if (networkErrorHasOccurred) {
                    showErrorSnackbar();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_sort) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Sort by")
                    .setItems(R.array.sort_options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(i == 0) mAdapter.sort(SortConfig.LAST_NAME);
                            if(i == 1) mAdapter.sort(SortConfig.FIRST_NAME);
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showErrorSnackbar() {
        String message = "Something went wrong. Check your internet connection.";
        Snackbar.make(mLinearLayout, message, Snackbar.LENGTH_LONG).show();
    }
}