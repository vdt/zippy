__author__ = 'zwei'

import sys, os, time

# setup paths
python_graph_path = os.path.join(os.path.dirname(__file__), 'python-graph')
python_graph_core_path = os.path.join(python_graph_path, 'core')
sys.path.append(python_graph_path)
sys.path.append(python_graph_core_path)

import pygraph
from pygraph.algorithms.accessibility import accessibility
from pygraph.classes.graph import graph
from sys import getrecursionlimit

def test_accessibility_on_very_deep_graph():
    gr = graph()
    gr.add_nodes(range(0,311)) # 2001
    for i in range(0,310): #2000
        gr.add_edge((i,i+1))
    recursionlimit = getrecursionlimit()
    accessibility(gr)
    assert getrecursionlimit() == recursionlimit

def build_graph():
    gr = graph()
    gr.add_nodes(range(0,311)) # 2001
    for i in range(0,310): #2000
        gr.add_edge((i,i+1))
    return gr

def call_accessibility(gr):
    recursionlimit = getrecursionlimit()
    accessibility(gr)
    assert getrecursionlimit() == recursionlimit

G = build_graph()

def main(n):
    for i in range(n):
        call_accessibility(G)

def measure():
    print("Start timing...")
    start = time.time()
    main(num)
    duration = "%.3f\n" % (time.time() - start)
    print("python-graph-access: " + duration)

# warm up
num =  int(sys.argv[1]) # 200
for i in range(50):
    main(20)

measure()
