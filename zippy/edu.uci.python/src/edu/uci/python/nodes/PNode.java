/*
 * Copyright (c) 2013, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.uci.python.nodes;

import java.math.BigInteger;

import org.python.core.PyObject;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.*;
import com.oracle.truffle.api.source.SourceSection;

import edu.uci.python.ast.VisitorIF;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.array.*;
import edu.uci.python.runtime.builtin.*;
import edu.uci.python.runtime.datatype.*;
import edu.uci.python.runtime.datatype.PSlice.PStartSlice;
import edu.uci.python.runtime.datatype.PSlice.PStopSlice;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.iterator.*;
import edu.uci.python.runtime.object.*;
import edu.uci.python.runtime.sequence.*;
import edu.uci.python.runtime.standardtype.*;

@TypeSystemReference(PythonTypes.class)
@ImportStatic(PGuards.class)
public abstract class PNode extends Node {

    @CompilationFinal private SourceSection sourceSection;

    @Override
    public String toString() {
        if (getSourceSection() != null)
            return getSourceSection().getSource().getName() + ":" + getSourceSection().getStartLine();
        else
            return super.toString();
    }

    @Override
    public SourceSection getSourceSection() {
        return this.sourceSection;
    }

    public abstract Object execute(VirtualFrame frame);

    public int executeInt(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);
        if (o instanceof Integer) {
            return (int) o;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        Object val = execute(frame);
        Object value = (val instanceof Integer) ? BigInteger.valueOf((int) val).longValue() : val;

        if (value instanceof Long) {
            return (long) value;
        } else {
            throw new UnexpectedResultException(val);
        }
    }

    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);
        if (o instanceof Double) {
            return (double) o;
        } else if (o instanceof Integer) {
            return (int) o;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public char executeCharacter(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);
        if (o instanceof Character) {
            return (char) o;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        Object o = execute(frame);

        if (o == Boolean.TRUE) {
            return true;
        } else if (o == Boolean.FALSE) {
            return false;
        } else {
            throw new UnexpectedResultException(o);
        }
    }

    public BigInteger executeBigInteger(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectBigInteger(execute(frame));
    }

    public String executeString(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectString(execute(frame));
    }

    public PString executePString(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPString(execute(frame));
    }

    public PComplex executePComplex(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPComplex(execute(frame));
    }

    public PBytes executeBytes(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPBytes(execute(frame));
    }

    public PDict executePDictionary(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPDict(execute(frame));
    }

    public PList executePList(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPList(execute(frame));
    }

    public PTuple executePTuple(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPTuple(execute(frame));
    }

    public PRange executePRange(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPRange(execute(frame));
    }

    public PSequence executePSequence(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPSequence(execute(frame));
    }

    public PSet executePSet(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPSet(execute(frame));
    }

    public PFrozenSet executePFrozenSet(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPFrozenSet(execute(frame));
    }

    public PBaseSet executePBaseSet(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPBaseSet(execute(frame));
    }

    public PIntArray executePIntArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPIntArray(execute(frame));
    }

    public PLongArray executePLongArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPLongArray(execute(frame));
    }

    public PDoubleArray executePDoubleArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPDoubleArray(execute(frame));
    }

    public PCharArray executePCharArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPCharArray(execute(frame));
    }

    public PArray executePArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPArray(execute(frame));
    }

    public PEnumerate executePEnumerate(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPEnumerate(execute(frame));
    }

    public PZip executePZip(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPZip(execute(frame));
    }

    public PStartSlice executePStartSlice(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPStartSlice(execute(frame));
    }

    public PStopSlice executePStopSlice(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPStopSlice(execute(frame));
    }

    public PSlice executePSlice(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPSlice(execute(frame));
    }

    public PGenerator executePGenerator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPGenerator(execute(frame));
    }

    public PDoubleIterator executePDoubleIterator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPDoubleIterator(execute(frame));
    }

    public PIntegerIterator executePIntegerIterator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPIntegerIterator(execute(frame));
    }

    public PIterator executePIterator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPIterator(execute(frame));
    }

    public PIterable executePIterable(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPIterable(execute(frame));
    }

    public PNone executePNone(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPNone(execute(frame));
    }

    public PythonBuiltinClass executePythonBuiltinClass(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPythonBuiltinClass(execute(frame));
    }

    public PythonBuiltinObject executePythonBuiltinObject(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPythonBuiltinObject(execute(frame));
    }

    public PythonModule executePythonModule(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPythonModule(execute(frame));
    }

    public PythonClass executePythonClass(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPythonClass(execute(frame));
    }

    public PythonObject executePythonObject(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPythonObject(execute(frame));
    }

    public PyObject executePyObject(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPyObject(execute(frame));
    }

    public Object[] executeObjectArray(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectObjectArray(execute(frame));
    }

    public PRangeIterator executePRangeIterator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPRangeIterator(execute(frame));
    }

    public PIntegerSequenceIterator executePIntegerSequenceIterator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPIntegerSequenceIterator(execute(frame));
    }

    public PLongSequenceIterator executePLongSequenceIterator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPLongSequenceIterator(execute(frame));
    }

    public PSequenceIterator executePSequenceIterator(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPSequenceIterator(execute(frame));
    }

    public PythonCallable executePythonCallable(VirtualFrame frame) throws UnexpectedResultException {
        return PythonTypesGen.expectPythonCallable(execute(frame));
    }

    public void executeVoid(VirtualFrame frame) {
        execute(frame);
    }

    public boolean hasSideEffectAsAnExpression() {
        return false;
    }

    public void clearSourceSection() {
        this.sourceSection = null;
    }

    public void assignSourceSection(SourceSection source) {
        this.sourceSection = source;
    }

    @SuppressWarnings("unused")
    public <R> R accept(VisitorIF<R> visitor) throws Exception {
        throw new RuntimeException("Unexpected PNode: " + this);
    }

    @SuppressWarnings("unused")
    public void traverse(VisitorIF<?> visitor) throws Exception {
        throw new RuntimeException("No viable traversal node: " + this);
    }

}
