package mobi.com.multiselect;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobi.com.multiselect.Adapters.AdapterListInbox;
import mobi.com.multiselect.model.Inbox;

public class MultiSelect extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdapterListInbox mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private List<Inbox> items = new ArrayList<>();
    private FloatingActionButton fab_Load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_select);

        initToolbar();
        initComponent();
        Toast.makeText(this, "Long press for multi selection", Toast.LENGTH_SHORT).show();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void addItems(){

        items.clear();
        items.add(new Inbox(1,R.drawable.photo_female_1, "Marcia Reyes","marciareyes@gmail.com","es simplemente el texto de relleno de las imprentas y archivos de texto","18 Nov.2018", R.color.red_500));
        items.add(new Inbox(2,R.drawable.photo_female_2, "Mery Lopez","merylopezs@gmail.com","es simplemente el texto de relleno de las imprentas y archivos de texto","18 Nov.2018", R.color.red_500));
        items.add(new Inbox(3,R.drawable.photo_female_3, "Mariana Perez","mperezs@gmail.com","es simplemente el texto de relleno de las imprentas y archivos de texto","18 Nov.2018", R.color.red_500));
        items.add(new Inbox(4,R.drawable.photo_male_1, "Jorge Ruiz","jruizs@gmail.com","es simplemente el texto de relleno de las imprentas y archivos de texto","18 Nov.2018", R.color.red_500));
        items.add(new Inbox(5,R.drawable.photo_male_2, "Marco Mendez","marcoperez@gmail.com","es simplemente el texto de relleno de las imprentas y archivos de texto","18 Nov.2018", R.color.red_500));
        items.add(new Inbox(6,R.drawable.photo_male_3, "Luis Ramirez","lramirezs@gmail.com","es simplemente el texto de relleno de las imprentas y archivos de texto","18 Nov.2018", R.color.red_500));
        items.add(new Inbox(7,null, "Linda Martinez","lindams@gmail.com","es simplemente el texto de relleno de las imprentas y archivos de texto","18 Nov.2018", R.color.red_500));

        mAdapter.notifyDataSetChanged();
    }

    private void initComponent() {
        fab_Load = findViewById(R.id.fab_refresh);
        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new AdapterListInbox(this, items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new AdapterListInbox.OnClickListener() {
            @Override
            public void onItemClick(View view, Inbox obj, int pos) {
                if (mAdapter.getSelectedItemCount() > 0) {
                    enableActionMode(pos);
                } else {
                    // read the inbox which removes bold from the row
                    Inbox inbox = mAdapter.getItem(pos);
                    Toast.makeText(getApplicationContext(), "Read: " + inbox.from, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, Inbox obj, int pos) {
                enableActionMode(pos);
            }
        });

        fab_Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItems();
            }
        });

        addItems();
        actionModeCallback = new ActionModeCallback();

    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(MultiSelect.this, R.color.colorDarkBlue2);
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                deleteInboxes();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(MultiSelect.this, R.color.colorPrimary);
        }
    }

    private void deleteInboxes() {
        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(getApplicationContext(), "Exiting The APP", Toast.LENGTH_SHORT).show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
