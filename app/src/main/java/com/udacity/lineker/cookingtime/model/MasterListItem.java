package com.udacity.lineker.cookingtime.model;

public class MasterListItem {
    private String description;
    private int stepPosition;
    private Receipt receipt;

    public MasterListItem(String description) {
        this.description = description;
    }

    public MasterListItem(String description, Receipt receipt, int stepPosition) {
        this.description = description;
        this.setReceipt(receipt);
        this.setStepPosition(stepPosition);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getStepPosition() {
        return stepPosition;
    }

    public void setStepPosition(int stepPosition) {
        this.stepPosition = stepPosition;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}
