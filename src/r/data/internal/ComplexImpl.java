package r.data.internal;

import r.*;
import r.Convert.ConversionStatus;
import r.data.*;


public class ComplexImpl extends NonScalarArrayImpl implements RComplex {

    final double[] content; // real0, imag0, real1, imag1, ...
    final int size;

    public ComplexImpl(double[] values, int[] dimensions, boolean doCopy) {
        if (doCopy) {
            content = new double[values.length];
            System.arraycopy(values, 0, content, 0, values.length);
        } else {
            content = values;
        }
        this.dimensions = dimensions;
        size = values.length / 2;
    }

    public ComplexImpl(double[] realValues, double[] imagValues, int[] dimensions) {
        Utils.check(realValues.length == imagValues.length);
        size = realValues.length;
        content = new double[size * 2];
        for (int i = 0; i < size; i++) {
            content[2 * i] = realValues[i];
            content[2 * i + 1] = imagValues[i];
        }
        this.dimensions = dimensions;
    }

    public ComplexImpl(double[] values, int[] dimensions) {
        this(values, dimensions, true);
    }

    public ComplexImpl(double[] values) {
        this(values, null, true);
    }

    public ComplexImpl(int size) {
        content = new double[2 * size];
        this.size = size;
    }

    public ComplexImpl(RComplex c) {
        size = c.size();
        content = new double[2 * size];
        for (int i = 0; i < size; i++) {
            content[2 * i] = c.getReal(i);
            content[2 * i + 1] = c.getImag(i);
        }
        dimensions = c.dimensions();
    }

    @Override
    public ComplexImpl stripAttributes() {
        if (dimensions == null) {
            return this;
        }
        if (!isShared()) {
            dimensions = null;
            return this;
        }
        ComplexImpl v = new ComplexImpl(content, null, false); // note: re-uses current values
        v.refcount = refcount; // mark the new integer shared
        return v;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object get(int i) {
        return new Complex(getReal(i), getImag(i));
    }

    @Override
    public RAny boxedGet(int i) {
        return RComplex.RComplexFactory.getScalar(getReal(i), getImag(i));
    }

    @Override
    public boolean isNAorNaN(int i) {
        return RComplex.RComplexUtils.eitherIsNAorNaN(getReal(i), getImag(i));
    }

    @Override
    public RArray set(int i, Object val) {
        Complex c = (Complex) val;
        return set(i, c.realValue(), c.imagValue());
    }

    @Override
    public RComplex set(int i, double real, double imag) {
        content[2 * i] = real;
        content[2 * i + 1] = imag;
        return this;
    }

    @Override
    public double getReal(int i) {
        return content[2 * i];
    }

    @Override
    public double getImag(int i) {
        return content[2 * i + 1];
    }

    @Override
    public ComplexImpl materialize() {
        return this;
    }

    @Override
    public String pretty() {
        if (dimensions != null) {
            return matrixPretty();
        }
        if (size == 0) {
            return RComplex.TYPE_STRING + "(0)";
        }
        String fst = Convert.pretty(Convert.complex2string(content[0], content[1]));
        if (size == 1) {
            return fst;
        }
        StringBuilder str = new StringBuilder();
        str.append(fst);
        for (int i = 1; i < size; i++) {
            str.append(", ");
            str.append(Convert.pretty(Convert.complex2string(content[2 * i], content[2 * i + 1])));
        }
        return str.toString();
    }

    @Override
    public RRaw asRaw() {
        return asRaw(null);
    }

    @Override
    public RRaw asRaw(ConversionStatus warn) {
        return RComplex.RComplexUtils.complexToRaw(this, warn);
    }

    @Override
    public RLogical asLogical() {
        return new RComplex.RLogicalView(this);
    }

    @Override
    public RLogical asLogical(ConversionStatus warn) {
        return asLogical();
    }

    @Override
    public RInt asInt() {
        return asInt(null);
    }

    @Override
    public RInt asInt(ConversionStatus warn) {
        return RComplex.RComplexUtils.complex2int(this, warn);
    }

    @Override
    public RDouble asDouble() {
        return asDouble(null);
    }

    @Override
    public RDouble asDouble(ConversionStatus warn) {
        return RComplex.RComplexUtils.complex2double(this, warn);
    }

    @Override
    public RComplex asComplex() {
        return this;
    }

    @Override
    public RComplex asComplex(ConversionStatus warn) {
        return this;
    }

    @Override
    public RString asString() {
        return new RComplex.RStringView(this);
    }

    @Override
    public RString asString(ConversionStatus warn) {
        return asString();
    }

    @Override
    public RArray subset(RInt index) {
        return RComplex.RComplexFactory.subset(this, index);
    }

    @Override
    public String typeOf() {
        return RComplex.TYPE_STRING;
    }

 }