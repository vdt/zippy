package edu.uci.python.profiler;

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

import java.util.*;

import com.oracle.truffle.api.CompilerDirectives.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.instrument.*;
import com.oracle.truffle.api.nodes.*;

/**
 * @author Gulfem
 */

public final class TypeDistributionProfilerInstrument extends Instrument {

    private final Node initialNode;
    private final Map<Class<? extends Node>, Counter> types;

    public TypeDistributionProfilerInstrument(Node initialNode) {
        this.initialNode = initialNode;
        this.types = new HashMap<>();
    }

    @Override
    public void leave(Node astNode, VirtualFrame frame) {
        addNewNodeOrIncrement(astNode);
    }

    @Override
    public void leave(Node astNode, VirtualFrame frame, boolean result) {
        leave(astNode, frame);
    }

    @Override
    public void leave(Node astNode, VirtualFrame frame, int result) {
        leave(astNode, frame);
    }

    @Override
    public void leave(Node astNode, VirtualFrame frame, double result) {
        leave(astNode, frame);
    }

    @Override
    public void leave(Node astNode, VirtualFrame frame, Object result) {
        leave(astNode, frame);
    }

    @Override
    public void leaveExceptional(Node astNode, VirtualFrame frame, Exception e) {
        leave(astNode, frame);
    }

    @SlowPath
    private void addNewNodeOrIncrement(Node astNode) {
        if (types.containsKey(astNode.getClass())) {
            types.get(astNode.getClass()).increment();
        } else {
            types.put(astNode.getClass(), new Counter());
        }
    }

    public Node getInitialNode() {
        return initialNode;
    }

    public Map<Class<? extends Node>, Counter> getTypes() {
        return types;
    }

}
