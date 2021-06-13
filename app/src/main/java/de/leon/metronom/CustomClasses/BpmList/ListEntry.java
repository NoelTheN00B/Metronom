package de.leon.metronom.CustomClasses.BpmList;

public class ListEntry {

    private Integer listEntryNumber = null;
    private String artist = "";
    private String song = "";
    private int bpm;
    private int tact;

    public ListEntry() { }

    public ListEntry(int bpm, int tact, String artist, String song) {
        this.bpm = bpm;
        this.tact = tact;
        this.artist = artist;
        this.song = song;
    }

    public ListEntry(int bpm, int tact) {
        new ListEntry(bpm, tact, null, null);
    }

    public int getListEntryNumber() {
        return listEntryNumber;
    }

    public int getBPM() {
        return bpm;
    }

    public int getTact() {
        return tact;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    protected void setListEntryNumber(int listEntryNumber) {
        this.listEntryNumber = listEntryNumber;
    }

    public void setBPM(int bpm) {
        this.bpm = bpm;
    }

    public void setTact(int bpm) {
        this.bpm = bpm;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setSong(String song) {
        this.song = song;
    }
}
