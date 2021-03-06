/*
 * Copyright (c) 2014, Regents of the University of California
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
package edu.uci.python.nodes.statement;

import org.python.core.*;

import com.oracle.truffle.api.frame.*;

import edu.uci.python.ast.VisitorIF;
import edu.uci.python.nodes.*;
import edu.uci.python.nodes.frame.*;
import edu.uci.python.runtime.function.*;
import edu.uci.python.runtime.object.*;
import edu.uci.python.runtime.sequence.*;

/**
 * @author Qunaibit
 * @author zwei
 *
 */
public class WithNode extends StatementNode {

    @Child protected PNode withContext;
    @Child protected PNode body;
    @Children private final PNode[] targetNodes;

    protected WithNode(PNode withContext, PNode[] targetNodes, PNode body) {
        this.withContext = withContext;
        this.targetNodes = targetNodes;
        this.body = body;
        assert withContext != null && body != null;
    }

    public static WithNode create(PNode withContext, PNode[] targetNodes, PNode body) {
        return new WithNode(withContext, targetNodes, body);
    }

    /**
     * zwei: This is bad design. Should make to looks more like {@link ApplyArgumentsNode}.
     */
    private void applyValues(VirtualFrame frame, Object asNameValue) {
        if (targetNodes.length == 0) {
            return;
        }

        if (targetNodes.length == 1) {
            ((WriteNode) targetNodes[0]).executeWrite(frame, asNameValue);
            return;
        }

        Object[] asNameValues = ((PTuple) asNameValue).getArray();
        for (int i = 0; i < targetNodes.length; i++) {
            WriteNode targetNode = (WriteNode) targetNodes[i];
            targetNode.executeWrite(frame, asNameValues[i]);
        }
    }

    public PNode getWithContext() {
        return withContext;
    }

    public PNode getBody() {
        return body;
    }

    public PNode[] getTargetNodes() {
        return targetNodes;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        RuntimeException exception = null;
        PythonObject pythonObj = (PythonObject) this.withContext.execute(frame);
        PythonCallable enterCall = (PythonCallable) pythonObj.getAttribute("__enter__");
        Object asNameValue = enterCall.call(PArguments.createWithUserArguments(pythonObj));
        applyValues(frame, asNameValue);

        try {
            body.execute(frame);
        } catch (RuntimeException e) {
            exception = e;
        } finally {
            PythonCallable exitCall = (PythonCallable) pythonObj.getAttribute("__exit__");

            if (exception instanceof ArithmeticException && exception.getMessage().endsWith("divide by zero")) {
                exception = Py.ZeroDivisionError("divide by zero");
            }

            Object returnValue = null;

            if (exception instanceof PyException) {
                Object type = ((PyException) exception).type;
                Object value = ((PyException) exception).value;
                Object trace = ((PyException) exception).traceback;
                returnValue = exitCall.call(PArguments.createWithUserArguments(pythonObj, type, value, trace));
            } else if (exception == null) {
                return exitCall.call(PArguments.createWithUserArguments(pythonObj));
            } else {
                throw exception;
            }

            // Corner cases:
            if (returnValue != null) {
                if (returnValue instanceof Boolean && ((Boolean) returnValue) == false) {
                    throw exception;
                }

                if (returnValue instanceof Integer && ((Integer) returnValue) == 0) {
                    throw exception;
                }

            } else {
                throw exception;
            }

        }
        return null;
    }

    @Override
    public <R> R accept(VisitorIF<R> visitor) throws Exception {
        return visitor.visitWithNode(this);
    }

}
