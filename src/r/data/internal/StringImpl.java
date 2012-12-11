package r.data.internal;

import r.*;
import r.Convert.NAIntroduced;
import r.Convert.OutOfRange;
import r.data.*;
import r.nodes.*;
import r.nodes.truffle.*;


public class StringImpl extends NonScalarArrayImpl implements RString {

    final String[] content;

    public StringImpl(String[] values, int[] dimensions, boolean doCopy) {
        if (doCopy) {
            content = new String[values.length];
            System.arraycopy(values, 0, content, 0, values.length);
        } else {
            content = values;
        }
        this.dimensions = dimensions;
    }

    public StringImpl(String[] values, int[] dimensions) {
        this(values, dimensions, true);
    }

    public StringImpl(String[] values) {
        this(values, null, true);
    }

    public StringImpl(int size) {
        content = new String[size];
    }

    public StringImpl(RString v, boolean valuesOnly) {
        content = new String[v.size()];
        for (int i = 0; i < content.length; i++) {
            content[i] = v.getString(i);
        }
        if (!valuesOnly) {
            dimensions = v.dimensions();
        }
    }

    @Override
    public StringImpl stripAttributes() {
        if (dimensions == null) {
            return this;
        }
        if (!isShared()) {
            dimensions = null;
            return this;
        }
        StringImpl v = new StringImpl(content, null, false); // note: re-uses current values
        v.refcount = refcount; // mark the new integer shared
        return v;
    }

    @Override
    public int size() {
        return content.length;
    }

    @Override
    public Object get(int i) {
        return content[i];
    }

    @Override
    public String getString(int i) {
        return content[i];
    }

    @Override
    public RAny boxedGet(int i) {
        return RString.RStringFactory.getScalar(getString(i));
    }

    @Override
    public boolean isNAorNaN(int i) {
        return content[i] == RString.NA;
    }

    @Override
    public StringImpl set(int i, String val) {
        content[i] = val;
        return this;
    }

    @Override
    public RArray set(int i, Object val) {
        content[i] = (String) val;
        return this;
    }

    @Override
    public StringImpl materialize() {
        return this;
    }

    @Override
    public String pretty() {
        if (dimensions != null) {
            return matrixPretty();
        }
        if (content.length == 0) {
            return RString.TYPE_STRING + "(0)";
        }
        StringBuilder str = new StringBuilder();
        if (content[0] != RString.NA) {
            str.append("\"");
            str.append(content[0]); // FIXME: quote
            str.append("\"");
        } else {
            str.append("NA");
        }
        for (int i = 1; i < content.length; i++) {
            str.append(", ");
            if (content[0] != RString.NA) {
                str.append("\"");
                str.append(content[i]); // FIXME: quote
                str.append("\"");
            } else {
                str.append("NA");
            }
        }
        return str.toString();
    }

    @Override
    public RRaw asRaw() {
        Utils.check(false, "unreachable");
        return null;
    }

    @Override
    public RRaw asRaw(NAIntroduced naIntroduced, OutOfRange outOfRange) {
        return RString.RStringUtils.stringToRaw(this, naIntroduced, outOfRange);
    }

    @Override
    public RLogical asLogical() {
        Utils.check(false, "unreachable");
        return null;
    }

    @Override
    public RLogical asLogical(NAIntroduced naIntroduced) {
        return RString.RStringUtils.stringToLogical(this, naIntroduced);
    }

    @Override
    public RInt asInt() {
        Utils.check(false, "unreachable");
        return null;
    }

    @Override
    public RInt asInt(NAIntroduced naIntroduced) {
        return RString.RStringUtils.stringToInt(this, naIntroduced);
    }


    @Override
    public RDouble asDouble() {
        Utils.check(false, "unreachable");
        return null;
    }

    @Override
    public RDouble asDouble(NAIntroduced naIntroduced) {
        return RString.RStringUtils.stringToDouble(this, naIntroduced);
    }

    @Override
    public RString asString() {
        return this;
    }

    @Override
    public RString asString(NAIntroduced naIntroduced) {
        return this;
    }

    @Override
    public <T extends RNode> T callNodeFactory(OperationFactory<T> factory) {
        Utils.nyi();  // FIXME: is callNodeFactory still used?
        return null;
    }

    @Override
    public RArray subset(RInt index) {
        return RString.RStringFactory.subset(this, index);
    }

    @Override
    public String typeOf() {
        return RString.TYPE_STRING;
    }
}
