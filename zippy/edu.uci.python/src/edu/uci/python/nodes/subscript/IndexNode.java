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
package edu.uci.python.nodes.subscript;

import java.math.BigInteger;

import org.python.core.*;

import com.oracle.truffle.api.dsl.*;

import edu.uci.python.ast.VisitorIF;
import edu.uci.python.nodes.expression.*;
import edu.uci.python.nodes.truffle.*;
import edu.uci.python.runtime.object.*;

@GenerateNodeFactory
public abstract class IndexNode extends UnaryOpNode {

    @Specialization
    public int doInteger(int index) {
        return index;
    }

    @Specialization
    public int doInteger(BigInteger index) {
        return index.intValue();
    }

    @SuppressWarnings("unused")
    @Specialization
    public double doDouble(double index) {
        throw Py.TypeError("list indices must be integers, not float");
    }

    @Specialization
    public String doString(String key) {
        return key;
    }

    @Specialization
    public Object doPythonObject(PythonObject clazz) {
        return clazz;
    }

    @Specialization
    public Object doObject(Object index) {
        throw Py.TypeError("list indices must be integers, not " + PythonTypesUtil.getPythonTypeName(index));
    }

    @Override
    public <R> R accept(VisitorIF<R> visitor) throws Exception {
        return visitor.visitIndexNode(this);
    }

}
