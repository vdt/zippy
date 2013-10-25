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
package edu.uci.python.runtime.datatypes;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;

public class PBuiltinFunction extends PCallable {

    private final CallTarget callTarget;

    public PBuiltinFunction(String name, CallTarget callTarget) {
        super(name, true);
        this.callTarget = callTarget;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public Object call(PackedFrame caller, Object[] args) {
        return callTarget.call(caller, new PArguments(PNone.NONE, args));
    }

    @Override
    public Object call(PackedFrame caller, Object[] args, Object[] keywords) {
        if (keywords.length == 0) {
            return callTarget.call(caller, new PArguments(PNone.NONE, args));
        } else {
            PKeyword[] pkeywords = new PKeyword[keywords.length];
            System.arraycopy(keywords, 0, pkeywords, 0, keywords.length);
            return callTarget.call(caller, new PArguments(PNone.NONE, args, pkeywords));
        }
    }

    public CallTarget getCallTarget() {
        return callTarget;
    }

    @Override
    public String toString() {
        return "<built-in function " + name + ">";
    }
}
