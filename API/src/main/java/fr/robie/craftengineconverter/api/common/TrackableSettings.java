package fr.robie.craftengineconverter.api.common;

public abstract class TrackableSettings {
    private int constructionHashCode = Integer.MIN_VALUE;
    private boolean initialized = false;

    protected void markInitialized() {
        this.constructionHashCode = computeHashCode();
        this.initialized = true;
    }

    public boolean isUpdated() {
        if (!this.initialized) return false;
        return this.constructionHashCode != computeHashCode();
    }

    protected abstract int computeHashCode();
}