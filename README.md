## GraphMaker ##

GraphMaker is an incomplete Java application to create graphs consisting of nodes
connected by directed and undirected edges, and perform various graph
operations on them, such as breadth-first-search and minimum-spanning-tree.

This application was never finished and in its current state accomplishes very little.

### Fibonacci Heap ###

The one remarkable thing about this project is that it has a very fast and correct
Java implementation of the [Fibonacci Heap](http://en.wikipedia.org/wiki/Fibonacci_heap)
data structure. It is found in the `core/src/com/bluemarsh/graphmaker/core/util` directory.

## TODO ##

- Implement the graphical editor
  - Look at Shapes Integration Sample in graph/integration
  - Set the TopComponent toolbar via the graph panel
  - Look for XXX to fix uses of Scene, and in widget factory
- How to represent the tools, and connect them to the view?
  - NB graph library has something for this.
  - Tools should appear in the editor toolbar
  - Tools are represented by Actions, which are added to the toolbar
    - Invoking the action tells the ToolManager to set the current tool
    - Tool attaches itself to the current document and view
  - Define a ToolManager service
    - manages the tools (set current, attach, detach)
    - does not persist anything
- Should the model component client properties be persisted?
  - If so, done in DefaultModelBeanInfo.Persistent class.
- Implement undo/redo support (hook in through top component)
- Create appropriate icons for the data loader, data node, wizard
- Recompile Windows launcher to take new icon.
- Update to latest NetBeans conventions
  - ErrorManager no longer used
