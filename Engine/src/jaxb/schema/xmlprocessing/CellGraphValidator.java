package jaxb.schema.xmlprocessing;

import jaxb.schema.generated.STLCell;
import jaxb.schema.generated.STLCells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;


/**
 * CellGraphValidator is responsible for validating and processing the graph of cells in a spreadsheet.
 * It detects circular references and performs topological sorting to determine the correct order of cell evaluations.
 */
public class CellGraphValidator {

    private int numOfVertices; // Number of vertices (cells) in the graph
    private int numOfEdges; // Number of edges (dependencies) in the graph
    private Map<String, List<String>> adjLists; // Adjacency list representing the graph structure
    private Map<String, STLCell> keyToSTLCellMap; // Map from cell keys to STLCell objects


    /**
     * Constructs a CellGraphValidator from the given STLCells object.
     * Initializes the graph with vertices and edges based on cell references.
     * @param stlCells the STLCells object containing the list of cells.*/
    public CellGraphValidator(STLCells stlCells) {
        this.numOfVertices = 0; // Initialize the number of vertices in the graph to 0
        this.numOfEdges = 0; // Initialize the number of edges in the graph to 0
        this.adjLists = new HashMap<>(); // Initialize the adjacency list map, which will hold the graph's structure
        this.keyToSTLCellMap = new HashMap<>(); // Initialize the map from keys to STLCell objects


        List<STLCell> listOfCells = stlCells.getSTLCell(); // Get the list of cells from the STLCells object
        // Add each cell as a vertex in the graph
        for (STLCell stlCell : listOfCells) {
            addVertex(stlCell);
        }
        // Add edges based on cell references
        for (STLCell stlCell : listOfCells) {
            String originalValue = stlCell.getSTLOriginalValue(); // Get the original value of the cell

            // Check if the original value starts with "{REF," and ends with "}"
            if (originalValue.startsWith("{REF,") && originalValue.endsWith("}")) {
                // Extract the referenced cell's key by trimming the "{REF," from the start and "}" from the end
                String referencedCell = originalValue.substring(5, originalValue.length() - 1).trim();

                // Find the cell in the list that matches the referenced cell's key
                STLCell refCell = listOfCells.stream()
                        .filter(cell -> getCellKey(cell).equals(referencedCell)) // Use the getCellKey method to get the key for comparison
                        .findFirst() // Find the first match
                        .orElse(null); // If no match is found, return null

                if (refCell != null) {
                    addEdge(refCell, stlCell); // If the referenced cell is found, add an edge from it to the current cell
                }
            }
        }
    }

    /**
     * Adds a vertex (cell) to the graph.
     * @param stlCell the cell to be added as a vertex.*/
    public void addVertex(STLCell stlCell) {
        String cellKey = getCellKey(stlCell);
        if (!adjLists.containsKey(cellKey)) {
            adjLists.put(cellKey, new ArrayList<>());
            keyToSTLCellMap.put(cellKey, stlCell);
            numOfVertices++;
        }
    }

    /**
     * Adds a directed edge between two vertices (cells) in the graph.
     * @param from the cell from which the edge originates.
     * @param to the cell to which the edge points.*/
    public void addEdge(STLCell from, STLCell to) {
        String fromKey = getCellKey(from);
        String toKey = getCellKey(to);

        // Add the edge
        if (!adjLists.get(fromKey).contains(toKey)) {
            adjLists.get(fromKey).add(toKey);
            numOfEdges++;
        }
    }

    /**
     * Generates a unique key for a cell based on its row and column.
     * This key is used to identify the cell in the adjacency list.
     * @param stlCell the cell for which to generate the key.
     * @return a String representing the unique key for the cell.*/
    public String getCellKey(STLCell stlCell) {
        int row = stlCell.getRow();
        char column =stlCell.getColumn().charAt(0);
        return column + ":" + row;
    }

    /**
     * Performs a topological sort on the graph of cells.
     * If a circular reference is detected, an exception is thrown.
     * @return a List of STLCell objects sorted in topological order.
     * @throws FileDataException.CircularReferenceException if a circular reference is detected.*/
    public List<STLCell> topologicalSort() throws FileDataException.CircularReferenceException {
        //inDegree map <key:cellKey, Value:cell inDegree rank)
        Map<String, Integer> inDegree = new HashMap<>();
        //Queue sources : Contains the keys to cells that are sources in the current graph
        Queue<String> queue = new LinkedList<>();
        //A list of the topological sort
        List<String> sortedList = new ArrayList<>();

        // Initialize all inDegree to 0
        for (String cellKey : adjLists.keySet()) {
            inDegree.put(cellKey, 0);
        }

        // Calculate inDegree
        for (String cellKey : adjLists.keySet()) {
            for (String neighbor : adjLists.get(cellKey)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // Add vertices with 0 inDegree to the queue
        for (String cellKey : inDegree.keySet()) {
            if (inDegree.get(cellKey) == 0) {
                queue.offer(cellKey);
            }
        }

        // Process the vertices
        while (!queue.isEmpty()) {
            String cellKey = queue.poll();
            sortedList.add(cellKey);

            for (String neighbor : adjLists.get(cellKey)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Check if all nodes were processed
        for (String cellKey : inDegree.keySet()) {
            if (inDegree.get(cellKey) != 0) {
                throw new FileDataException.CircularReferenceException();
            }
        }

        return convertToCellList(sortedList);
    }
    /**
     * Converts a list of cell keys (from topological sort) into a list of STLCell objects.
     * @param sortedKeys the list of sorted cell keys.
     * @return a List of STLCell objects corresponding to the sorted keys.*/
    public List<STLCell> convertToCellList(List<String> sortedKeys) {
        List<STLCell> stlCellList = new ArrayList<>();
        for (String key : sortedKeys) {
            STLCell stlcell = keyToSTLCellMap.get(key);
            if (stlcell != null) {
                stlCellList.add(stlcell);
            }
        }
        return stlCellList;
    }
}
