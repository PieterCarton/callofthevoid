package com.example.examplemod.essence;

public class EssenceQuantity {
    private final EssenceType type;
    private int quantity;

    public EssenceType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public EssenceQuantity(EssenceType type) {
        this(type, 0);
    }

    public EssenceQuantity(EssenceType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public boolean canMerge(EssenceQuantity toMerge) {
        return toMerge.getType() == this.type;
    }

    public void merge(EssenceQuantity toMerge) {
        quantity += toMerge.getQuantity();
        toMerge.setQuantity(0);
    }

    public EssenceQuantity split(int amount) {
        int available = Math.min(amount, quantity);
        quantity -= available;
        return new EssenceQuantity(this.type, available);
    }
}
