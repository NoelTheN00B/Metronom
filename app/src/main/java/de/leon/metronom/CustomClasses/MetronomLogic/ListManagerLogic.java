package de.leon.metronom.CustomClasses.MetronomLogic;

import java.util.ArrayList;
import java.util.List;

import de.leon.metronom.CustomClasses.MetronomLogic.BpmList.BpmList;
import de.leon.metronom.CustomClasses.MetronomLogic.BpmList.ListEntry;

public class ListManagerLogic {

    private List<BpmList> bpmLists = new ArrayList<>();
    private BpmList bpmList = new BpmList();
    private BpmList selectedList = new BpmList();

    public ListManagerLogic() {
    }

    public void addEntry(String bpm, String tact, String artist, String song, String listName) {

        if (bpmList == null) {
            bpmList = new BpmList();
        }

        bpmList.addListEntry(new ListEntry(Integer.parseInt(bpm), Integer.parseInt(tact), artist, song));

        if (bpmList.getName() == null || bpmList.getName().isEmpty() || !bpmList.getName().equals(listName)) {
            bpmList.setName(listName);
        }
    }

    public List<BpmList> getBpmLists() {
        return bpmLists;
    }

    public void setBpmLists(List<BpmList> bpmLists) {
        this.bpmLists = bpmLists;
    }

    public BpmList getBpmList() {
        return bpmList;
    }

    public void setBpmList(BpmList bpmList) {
        this.bpmList = bpmList;
    }

    public BpmList getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(BpmList selectedList) {
        this.selectedList = selectedList;
    }

    //Enums
    public enum TextFields {
        BPM,
        TACT,
        ARTIST,
        SONG,
        LIST_NAME;
    }
}
