package de.leon.metronom.CustomClasses.BpmList;

import android.os.Build;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BpmList {

    private List<ListEntry> listEntries = new ArrayList<>();
    private String name;
    private Integer entries = 0;
    private LocalDateTime creationDate;
    private static final String VERSION = "1.0";

    public BpmList() {
        generateCreationDate();
    }

    public BpmList(String name) {
        setName(name);
        generateCreationDate();
    }

    public BpmList(String name, List<ListEntry> listEntries) {
        setName(name);
        setListEntries(listEntries);
        generateCreationDate();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addListEntry(ListEntry listEntry) {
        entries++;
        listEntry.setListEntryNumber(entries);
        this.listEntries.add(listEntry);
    }

    public void addListEntries(List<ListEntry> listEntries) {
        for (ListEntry listEntry : listEntries) {
            addListEntry(listEntry);
        }
    }

    public void setListEntries(List<ListEntry> listEntries) {
        addListEntries(listEntries);
    }

    public List<ListEntry> getListEntries() {
        return this.listEntries;
    }

    public ListEntry getListEntry(int listPosition) {
        return (ListEntry) this.listEntries.get(listPosition);
    }

    public BpmList getList() {
        return this;
    }

    public String getVersion() {
        return VERSION;
    }

    private void generateCreationDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            creationDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public Integer getEntries() {
        return entries;
    }
}
