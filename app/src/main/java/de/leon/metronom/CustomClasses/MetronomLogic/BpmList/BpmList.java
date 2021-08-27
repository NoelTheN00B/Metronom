package de.leon.metronom.CustomClasses.MetronomLogic.BpmList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BpmList {

    private List<ListEntry> listEntries = new ArrayList<>();
    private String name = "New List";
    private Integer entries = 0;
    private Date creationDate;
    private static final String VERSION = "1.0";

    public BpmList() {
        this(null, null);
    }

    public BpmList(String name) {
        this(name, null);
    }

    public BpmList(List<ListEntry> listEntries) {
        this(null, listEntries);
    }

    public BpmList(String name, List<ListEntry> listEntries) {
        this.entries = 0;
        this.listEntries = listEntries;
        this.name = name == null ? "New List" : name;
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
        this.listEntries = new ArrayList<>();
        this.entries = 0;
        addListEntries(listEntries);
    }

    public List<ListEntry> getListEntries() {
        return this.listEntries;
    }

    public ListEntry getListEntry(int listPosition) {
        return this.listEntries.get(listPosition);
    }

    public BpmList getList() {
        return this;
    }

    public String getVersion() {
        return VERSION;
    }

    private void generateCreationDate() {
        creationDate = Calendar.getInstance().getTime();
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Integer getEntries() {
        return entries;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
