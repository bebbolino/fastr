package r.data.internal;

import r.*;
import r.Convert.NAIntroduced;
import r.Convert.OutOfRange;
import r.data.*;
import r.nodes.*;
import r.nodes.truffle.*;

public class LogicalImpl extends NonScalarArrayImpl implements RLogical {

    final int[] content;

    public LogicalImpl(int size) {
        content = new int[size];
    }

    public LogicalImpl(int[] values, int[] dimensions, boolean doCopy) {
        if (doCopy) {
            content = new int[values.length];
            System.arraycopy(values, 0, content, 0, values.length);
        } else {
            content = values;
        }
        this.dimensions = dimensions;
    }

    public LogicalImpl(int[] values, int[] dimensions) {
        this(values, dimensions, true);
    }

    public LogicalImpl(int[] values) {
        this(values, null, true);
    }

    public LogicalImpl(RLogical l) {
        content = new int[l.size()];
        for (int i = 0; i < content.length; i++) {
            content[i] = l.getLogical(i);
        }
        dimensions = l.dimensions();
    }

    @Override
    public LogicalImpl stripAttributes() {
        if (dimensions == null) {
            return this;
        }
        if (!isShared()) {
            dimensions = null;
            return this;
        }
        LogicalImpl v = new LogicalImpl(content, null, false); // note: re-uses current values
        v.refcount = refcount; // mark the new vector shared
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
    public int getLogical(int i) {
        return content[i];
    }

    @Override
    public RAny boxedGet(int i) {
        return RLogicalFactory.getScalar(getLogical(i));
    }

    @Override
    public boolean isNAorNaN(int i) {
        return content[i] == RLogical.NA;
    }

    @Override
    public RArray set(int i, Object val) {
        return set(i, ((Integer) val).intValue()); // FIXME better conversion
    }

    @Override
    public RLogical set(int i, int val) {
        content[i] = val;
        return this;
    }

    @Override
    public RRaw asRaw() {
        return new RLogical.RRawView(this);
    }

    @Override
    public RRaw asRaw(NAIntroduced naIntroduced, OutOfRange outOfRange) {
        return RLogical.RLogicalUtils.logicalToRaw(this, outOfRange);
    }

    @Override
    public RLogical asLogical() {
        return this;
    }

    @Override
    public RLogical asLogical(NAIntroduced naIntroduced) {
        return this;
    }

    @Override
    public RInt asInt() {
        return RInt.RIntFactory.getFor(content, dimensions());
    }

    @Override
    public RInt asInt(NAIntroduced naIntroduced) {
        return asInt();
    }

    @Override
    public RDouble asDouble() {
        return new RLogical.RDoubleView(this);
    }

    @Override
    public RDouble asDouble(NAIntroduced naIntroduced) {
        return asDouble();
    }

    @Override
    public RString asString() {
        return new RLogical.RStringView(this);
    }

    @Override
    public RString asString(NAIntroduced naIntroduced) {
        return asString();
    }

    @Override
    public LogicalImpl materialize() {
        return this;
    }

    @Override
    public String pretty() {
        if (dimensions != null) {
            return matrixPretty();
        }
        if (content.length == 0) {
            return RLogical.TYPE_STRING + "(0)";
        }
        String fst = Convert.pretty(Convert.logical2string(content[0]));
        if (content.length == 1) {
            return fst;
        }
        StringBuilder str = new StringBuilder();
        str.append(fst);
        for (int i = 1; i < content.length; i++) {
            str.append(", ");
            str.append(Convert.pretty(Convert.logical2string(content[i])));
        }
        return str.toString();
    }

    @Override
    public <T extends RNode> T callNodeFactory(OperationFactory<T> factory) {
        return factory.fromLogical();
    }

    @Override
    public RArray subset(RInt index) {
        return RLogical.RLogicalFactory.subset(this, index);
    }

    @Override
    public String typeOf() {
        return RLogical.TYPE_STRING;
    }
}
