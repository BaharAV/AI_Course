package algorithms.search;

import algorithms.Algorithm;
import game.node;

import java.util.ArrayList;
import java.util.LinkedList;

public class BFS implements Algorithm {
    int call = 0;
    int go;
//    int i = 0;
    LinkedList<node> open = new LinkedList<>();
    LinkedList<node> close = new LinkedList<>();
    ArrayList<node> path = new ArrayList<>();
    ArrayList<node> way;
    node begin, last;

    void bfs() {
        way = new ArrayList<>();
        open.add(begin);
        while (!open.isEmpty()) {
//            System.out.println(i);
//            i++;
            node u = open.peek();
            open.remove(u);
            close.add(u);
            u.makechildren();
            for (int i = 0; i < u.child.size(); i++) {
                if (!close.contains(u.child.get(i))) {
                    open.add(u.child.get(i));
                    way.add(u.child.get(i));
                    if (last.equals(u.child.get(i))) {
                        last.parent = u;
                        return;
                    }
                }
            }
        }
    }

    String desidetypr() {
        if (path.get(go).emptyX == path.get(go + 1).emptyX - 1)
            return "LEFT";
        else if (path.get(go).emptyX == path.get(go + 1).emptyX + 1)
            return "RIGHT";
        else if (path.get(go).emptyY == path.get(go + 1).emptyY - 1)
            return "UP";
        else if (path.get(go).emptyY == path.get(go + 1).emptyY + 1)
            return "DOWN";
        return null;
    }

    @Override
    public String makeMove(String[][] grid) {
        if (call == 0) {
            call = 1;
            String result[][] = new String[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    result[i][j] = String.valueOf(i * 3 + j + 1);
                }
            }
            result[2][2] = " ";
            begin = new node(grid, null);
            last = new node(result, null);
            path.add(last);
            bfs();
            while (true) {
                if (path.get(path.size() - 1).parent != null)
                    path.add(path.get(path.size() - 1).parent);
                else
                    break;
            }
            go = path.size() - 2;
            return desidetypr();
        } else {
            go--;
            return desidetypr();
        }
    }
}
