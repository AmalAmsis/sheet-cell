package jaxb.schema.xmlprocessing;

import jaxb.schema.generated.STLCell;
import jaxb.schema.generated.STLCells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;


public class CellGraphValidator {
    /*
    אמל אני כותבת לך הסברים בעברית כדי שתביני ואז תמחקי.
        data member:
        numOfVertices - מספר התאים שיש בגיליון
        numOfEdges - מס' הקשתות - הצבעות לתאים
        adjLists - מפה של רשימות שכנים.

        methods:
        1) Ctor - מקבל את האובייקט שמכיל את רשימת התאים ויודע לייצר ממנו את הגרף.
        שימי לב שכרגע הוא מייצר רק את הקודקודים, בניתי פונקציה תמימה של הובפת קשת מ-  ל-  .. יכולה להעזר בה אחרי שאת מפעילה את הלוגיקה.
        2) String getCellKey(STLCell stlCell) -  כל פעם שאת רוצה לבדוק רשימת שכנים של תא מסויים,
        את חייבת להשתמש בפונקציה הזאת כדי להגיע למפתח שיתן לך את הרשימה.
        3) addVertex
        4) addEdge

     */

    private int numOfVertices;
    private int numOfEdges;
    private Map<String, List<String>> adjLists;
    private Map<String,STLCell> keyToSTLCellMap;

    //נכון לעכשיו הקונסטרקטור מייצר גרף רק עם קודקודים. יכולה להוסיף בפנים את המימוש להוספת הקשתות.
    public CellGraphValidator(STLCells stlCells) {
        this.numOfVertices = 0; // Initialize the number of vertices in the graph to 0
        this.numOfEdges = 0; // Initialize the number of edges in the graph to 0
        this.adjLists = new HashMap<>(); // Initialize the adjacency list map, which will hold the graph's structure
        this.keyToSTLCellMap = new HashMap<>(); // Initialize the map from keys to STLCell objects


        List<STLCell> listOfCells = stlCells.getSTLCell(); // Get the list of cells from the STLCells object

        for (STLCell stlCell : listOfCells) {
            addVertex(stlCell); // Add each cell as a vertex in the graph
        }

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

    // Add a vertex to the graph
    public void addVertex(STLCell stlCell) {
        String cellKey = getCellKey(stlCell);
        if (!adjLists.containsKey(cellKey)) {
            adjLists.put(cellKey, new ArrayList<>());
            keyToSTLCellMap.put(cellKey, stlCell);
            numOfVertices++;
        }
    }

    // Add an edge between two vertices
    public void addEdge(STLCell from, STLCell to) {
        String fromKey = getCellKey(from);
        String toKey = getCellKey(to);

        // Add the vertices if they don't exist
        // אם עושים את זה יכול ליצור טעויות?
        // אם אני עושה REF לתא שאין בו תוכן זה שגיאה?
        addVertex(from);
        addVertex(to);

        // Add the edge
        if (!adjLists.get(fromKey).contains(toKey)) {
            adjLists.get(fromKey).add(toKey);
            numOfEdges++;
        }
    }

    //כל פעם שאת רוצה לבדוק רשימת שכנים של תא מסויים, את חייבת להשתמש בפונקציה הזאת כדי להגיע למפתח שיתן לך את הרשימה.
    public String getCellKey(STLCell stlCell) {
        int row = stlCell.getRow();
        char column =stlCell.getColumn().charAt(0);
        return column + ":" + row;
    }

    //מיון טופולוגי - מחזיר רשימה של סדר המיון או Null אם יש מעגל
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
