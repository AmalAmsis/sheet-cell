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

    //נכון לעכשיו הקונסטרקטור מייצר גרף רק עם קודקודים. יכולה להוסיף בפנים את המימוש להוספת הקשתות.
    public CellGraphValidator(STLCells stlCells) {
        this.numOfVertices = 0;
        this.numOfEdges = 0;
        this.adjLists = new HashMap<>();

        List<STLCell> listOfCells = stlCells.getSTLCell();

        for(STLCell stlCell : listOfCells)
        {
            addVertex(stlCell);
            this.numOfVertices++;
        }

        //מימוש הוספת הקשתות

    }

    // Add a vertex to the graph
    public void addVertex(STLCell stlCell) {
        String cellKey = getCellKey(stlCell);
        if (!adjLists.containsKey(cellKey)) {
            adjLists.put(cellKey, new ArrayList<>());
            numOfVertices++;
        }
    }

    // Add an edge between two vertices
    public void addEdge(STLCell from, STLCell to) {
        String fromKey = getCellKey(from);
        String toKey = getCellKey(to);

        // Add the vertices if they don't exist
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
    public List<String> topologicalSort() {
        Map<String, Integer> inDegree = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
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
                return null; // Return null if a cycle is detected
            }
        }

        return sortedList;
    }
}
