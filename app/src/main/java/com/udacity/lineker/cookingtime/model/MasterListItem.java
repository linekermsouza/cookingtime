package com.udacity.lineker.cookingtime.model;

public class MasterListItem {
    private String description;
    private Step step;

    public MasterListItem(String description, Step step) {
        this.description = description;
        this.step = step;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }
}
