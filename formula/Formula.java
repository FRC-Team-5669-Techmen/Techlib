package edu.boscotech.techlib.formula;

public abstract class Formula {
    private double mCachedValue;
    private long mCacheStamp = 0;
    private static long sCacheStamp = 1;

    protected abstract double computeValue();

    private boolean isCacheValid() { return mCacheStamp >= sCacheStamp; }

    private void setCacheValid() { mCacheStamp = sCacheStamp; }

    public static void invalidateAllCaches() { sCacheStamp++; }

    public final double getValue() {
        if (!isCacheValid()) {
            mCachedValue = computeValue();
            setCacheValid();
        }
        return mCachedValue;
    }
}