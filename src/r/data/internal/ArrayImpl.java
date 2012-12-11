package r.data.internal;

import r.*;
import r.Convert.NAIntroduced;
import r.Convert.OutOfRange;
import r.data.*;


public abstract class ArrayImpl extends BaseObject implements RArray {
    @Override
    public RArray subset(RAny keys) {
        if (keys instanceof RInt) {
            return subset((RInt) keys);
        } else if (keys instanceof RDouble) {
            return subset(((RDouble) keys).asInt());
        } else if (keys instanceof RString) {
            return subset((RString) keys);
        } else {
            Utils.nyi();
        }

        return null;
    }

    @Override
    public RArray subset(RString names) {
        Utils.nyi();
        return null;
    }

    @Override
    public RArray subset(RInt index) {
        Utils.nyi();
        return null;
    }

    @Override
    public RArray materialize() {
        return this;
    }

    @Override
    public int[] dimensions() {
        return null;
    }

    @Override
    public RArray setDimensions(int[] dimensions) {
        Utils.nyi("not supported");
        return null;
    }

    @Override
    public int index(int i, int j) { // i-th row, j-th column indexed from 1
        int[] dims = dimensions();
        return i + (j - 1) * dims[1];
    }

    @Override
    public void ref() {
    }

    @Override
    public boolean isShared() {
        return true;
    }

    @Override
    public RArray stripAttributes() {
        return this;
    }

    @Override
    public Object getRef(int i) {
        return get(i);
    }

    @Override
    public RRaw asRaw(NAIntroduced naIntroduced, OutOfRange outOfRange) {
        Utils.nyi();
        return null;
    }

    @Override
    public RLogical asLogical(NAIntroduced naIntroduced) {
        Utils.nyi();
        return null;
    }

    @Override
    public RInt asInt(NAIntroduced naIntroduced) {
        Utils.nyi();
        return null;
    }

    @Override
    public RDouble asDouble(NAIntroduced naIntroduced) {
        Utils.nyi();
        return null;
    }

    @Override
    public RString asString(NAIntroduced naIntroduced) {
        Utils.nyi();
        return null;
    }

    @Override
    public RList asList() {
        return new RArray.RListView(this);
    }

}
