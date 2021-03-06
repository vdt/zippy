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
package edu.uci.python.test.generator;

import static edu.uci.python.test.PythonTests.assertPrints;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeUtil;

import edu.uci.python.nodes.generator.ComprehensionNode.ListComprehensionNode;
import edu.uci.python.runtime.PythonOptions;
import edu.uci.python.runtime.PythonParseResult;

public class BuiltinIntrinsificationTests {

    @Test
    public void simpleListComp() {
        assertTrue(!PythonOptions.isEnvOptionSet("disableIntrinsifyBuiltinCalls"));
        String source = "for x in range(2):\n" + //
                        "    ll = list(i for i in range(5))\n" + //
                        "print(ll)";
        PythonParseResult ast = assertPrints("[0, 1, 2, 3, 4]\n", source);
        Node listComp = NodeUtil.findFirstNodeInstance(ast.getModuleRoot(), ListComprehensionNode.class);
        assertTrue(listComp != null);
    }

    @Test
    public void listComp() {
        assertTrue(!PythonOptions.isEnvOptionSet("disableIntrinsifyBuiltinCalls"));
        String[] options = {"disableOptimizeGeneratorExpressions"};
        PythonOptions.setEnvOptions(options);
        Path script = Paths.get("builtin-list-intrinsification-test.py");
        assertPrints("9\n", script);
        PythonOptions.unsetEnvOptions(options);
    }

}
