package de.leon.metronom;

import android.content.Context;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.leon.metronom.CustomClasses.MetronomLogic.BpmList.BpmList;
import de.leon.metronom.CustomClasses.MetronomLogic.BpmList.ListEntry;
import de.leon.metronom.CustomClasses.MetronomLogic.ListManagerLogic;

public class ListManagerActivity extends AppCompatActivity implements ListManagerListAdapter.ItemClickListener {

    //private ListManagerListAdapter adapter;
    private ListManagerLogic logic;

    private RecyclerView listmanagerList;
    private TextView listNameFld;
    private TextView artistFld;
    private TextView songFld;
    private TextView bpmFld;
    private TextView tactFld;
    private Button addEntryBtn;
    private Button saveBtn;
    private Button newBtn;
    private Button deleteSelectedListBtn;

    private void connectAllUiElements() {

        listmanagerList = (RecyclerView) findViewById(R.id.listmanagerList);
        listNameFld = (TextView) findViewById(R.id.txtFldListName);
        artistFld = (TextView) findViewById(R.id.txtFldArtist);
        songFld = (TextView) findViewById(R.id.txtFldSong);
        bpmFld = (TextView) findViewById(R.id.txtFldBpmListEntry);
        tactFld = (TextView) findViewById(R.id.txtFldTactListEntry);
        addEntryBtn = (Button) findViewById(R.id.btnAddEntry);
        saveBtn = (Button) findViewById(R.id.btnSave);
        newBtn = (Button) findViewById(R.id.btnNew);
        deleteSelectedListBtn = (Button) findViewById(R.id.btnDeleteSelectedList);

        listmanagerList.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new ListManagerListAdapter(this, logic.getBpmLists());
        //adapter.setClickListener(this);
        //listmanagerList.setAdapter(adapter);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listmanagerList.getContext(), getRequestedOrientation());
        //listmanagerList.addItemDecoration(dividerItemDecoration);

        setClickListener();
    }

    private void setClickListener() {

        newBtn.setOnClickListener(v -> {
            logic.setBpmList(new BpmList());

            try {
                listNameFld.clearComposingText();
                bpmFld.clearComposingText();
                tactFld.clearComposingText();
                artistFld.clearComposingText();
                songFld.clearComposingText();
            } catch (Exception ignore) {}
        });

        addEntryBtn.setOnClickListener(v -> addEntry());

        saveBtn.setOnClickListener(v -> saveBpmList());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_manager);

        connectAllUiElements();

        listmanagerList.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new ListManagerListAdapter(this, logic.getBpmLists());
        //adapter.setClickListener(this);
        //listmanagerList.setAdapter(adapter);
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listmanagerList.getContext(), getRequestedOrientation());
        //listmanagerList.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        logic = new ListManagerLogic();

        connectAllUiElements();
    }

    @Override
    public void onItemClick(View view, int position) {
        //logic.setSelectedList(adapter.getItem(position));
    }

    private void addEntry() {

        logic.addEntry(bpmFld.getText().toString(), tactFld.getText().toString(),
                artistFld.getText().toString(), songFld.getText().toString(),
                listNameFld.getText().toString());

        bpmFld.setText("");
        tactFld.setText("");
        artistFld.setText("");
        songFld.setText("");
    }

    private void saveBpmList() {

        BpmList listToSave = logic.getBpmList();

        if (listToSave.getName().isEmpty()) {
            Toast.makeText(ListManagerActivity.this, getString(R.string.error) + ": " + getString(R.string.err_list_name_empty), Toast.LENGTH_LONG).show();
        }

        if (loadBpmList(listToSave.getName()) != null) {
            Toast.makeText(ListManagerActivity.this, getString(R.string.error) + ": " + getString(R.string.err_list_name_already_taken), Toast.LENGTH_LONG).show();
            return;
        }

        try {
            FileOutputStream fileOutputStream = openFileOutput(listToSave.getName() + ".xml", Context.MODE_APPEND);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream, "UTF-8");
            serializer.startDocument(null, Boolean.TRUE);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "root");

            serializer.startTag(null, "name");
            serializer.text(listToSave.getName());
            serializer.endTag(null, "name");

            serializer.startTag(null, "entries");
            serializer.text(listToSave.getEntries().toString());
            serializer.endTag(null, "entries");

            serializer.startTag(null, "listEntries");
            for (ListEntry listEntry : listToSave.getListEntries()) {
                serializer.startTag(null, "listEntry");

                serializer.startTag(null, "listEntryNumber");
                serializer.text(listEntry.getListEntryNumber() + "");
                serializer.endTag(null, "listEntryNumber");

                serializer.startTag(null, "artist");
                serializer.text(listEntry.getArtist());
                serializer.endTag(null, "artist");

                serializer.startTag(null, "song");
                serializer.text(listEntry.getSong());
                serializer.endTag(null, "song");

                serializer.startTag(null, "bpm");
                serializer.text(listEntry.getBPM() + "");
                serializer.endTag(null, "bpm");

                serializer.startTag(null, "tact");
                serializer.text(listEntry.getTact() + "");
                serializer.endTag(null, "tact");

                serializer.endTag(null, "listEntry");
            }
            serializer.endTag(null, "listEntries");

            serializer.startTag(null, "creationDate");
            serializer.text(listToSave.getCreationDate().toString());
            serializer.endTag(null, "creationDate");

            serializer.startTag(null, "VERSION");
            serializer.text(listToSave.getVersion());
            serializer.endTag(null, "VERSION");

            serializer.endTag(null, "root");

            serializer.endDocument();
            serializer.flush();

            fileOutputStream.close();

            Toast.makeText(ListManagerActivity.this, getString(R.string.saved), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ListManagerActivity.this, getString(R.string.unkwn_err), Toast.LENGTH_LONG).show();
        }
    }

    public BpmList loadBpmList(String fileName) {
        BpmList bpmList = null;

        try {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(fileName + ".xml");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            char[] inputBuffer = new char[fileInputStream.available()];
            inputStreamReader.read(inputBuffer);

            String data = new String(inputBuffer);

            inputStreamReader.close();
            fileInputStream.close();

            InputStream inputStream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);

            document.getDocumentElement().normalize();

            NodeList name = document.getElementsByTagName("name");
            NodeList entries = document.getElementsByTagName("entries");
            NodeList creationDate = document.getElementsByTagName("creationDate");
            NodeList version = document.getElementsByTagName("VERSION"); //use later when a newer version is used

            NodeList listEntryArtits = document.getElementsByTagName("artist");
            NodeList listEntrySongs = document.getElementsByTagName("song");
            NodeList listEntryBpms = document.getElementsByTagName("bpm");
            NodeList listEntryTacts = document.getElementsByTagName("tact");

            bpmList = new BpmList(name.item(0).getNodeValue());
            bpmList.setCreationDate(Date.valueOf(creationDate.item(0).getNodeValue()));
            for (int i = 0; i < Integer.parseInt(entries.item(0).getNodeValue()); i++) {
                bpmList.addListEntry(new ListEntry(
                        Integer.parseInt(listEntryBpms.item(i).getNodeValue()),
                        Integer.parseInt(listEntryTacts.item(i).getNodeValue()),
                        listEntryArtits.item(i).getNodeValue(),
                        listEntrySongs.item(i).getNodeValue()));
            }
            inputStream.close();

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        logic.setBpmList(bpmList);
        return bpmList;
    }
}