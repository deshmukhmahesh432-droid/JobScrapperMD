package com.example.demo.entity;


public class ScraperResultDTO {

    private int found;
    private int saved;
    private int duplicates;
    private int failed;

    public int getFound() { return found; }
    public void setFound(int found) { this.found = found; }

    public int getSaved() { return saved; }
    public void setSaved(int saved) { this.saved = saved; }

    public int getDuplicates() { return duplicates; }
    public void setDuplicates(int duplicates) {
        this.duplicates = duplicates;
    }

    public int getFailed() { return failed; }
    public void setFailed(int failed) {
        this.failed = failed;
    }
}
