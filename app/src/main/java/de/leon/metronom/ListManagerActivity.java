package de.leon.metronom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.leon.metronom.CustomClasses.BpmList.BpmList;

public class ListManagerActivity extends AppCompatActivity implements ListmanagerListAdapter.ItemClickListener {

    ListmanagerListAdapter adapter;
    List<BpmList> bpmLists = new ArrayList<>();
    BpmList selectedList;

    RecyclerView listmanagerList;
    TextView listNameFld;
    TextView artistFld;
    TextView songFld;
    TextView bpmFld;
    TextView tactFld;
    Button addEntryBtn;
    Button saveBtn;
    Button newBtn;
    Button deleteSelectedListBtn;

    private void connectAllUiElements() {

        listmanagerList = (RecyclerView) findViewById(R.id.listmanagerList);
        listNameFld = (TextView) findViewById(R.id.txtFldListName);
        artistFld = (TextView) findViewById(R.id.txtFldArtist);
        songFld = (TextView) findViewById(R.id.txtFldSong);
        bpmFld = (TextView) findViewById(R.id.txtFldBPM);
        tactFld = (TextView) findViewById(R.id.txtFldTact);
        addEntryBtn = (Button) findViewById(R.id.btnAddEntry);
        saveBtn = (Button) findViewById(R.id.btnSave);
        newBtn = (Button) findViewById(R.id.btnNew);
        deleteSelectedListBtn = (Button) findViewById(R.id.btnDeleteSelectedList);

        listmanagerList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListmanagerListAdapter(this, bpmLists);
        adapter.setClickListener(this);
        listmanagerList.setAdapter(adapter);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listmanagerList.getContext(), getRequestedOrientation());
        //listmanagerList.addItemDecoration(dividerItemDecoration);

        setClickListener();
    }

    private void setClickListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_manager);

        connectAllUiElements();

        listmanagerList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListmanagerListAdapter(this, bpmLists);
        adapter.setClickListener(this);
        listmanagerList.setAdapter(adapter);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listmanagerList.getContext(), getRequestedOrientation());
        //listmanagerList.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        connectAllUiElements();
    }

    @Override
    public void onItemClick(View view, int position) {
        selectedList = adapter.getItem(position);
    }
}