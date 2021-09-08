package algorithms.search;

import algorithms.Algorithm;
import game.node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class DFS implements Algorithm {
    int call = 0;
    int go;
    int i = 0;
    boolean found = false;
    Stack<node> open = new Stack<>();
    LinkedList<node> close = new LinkedList<>();
    ArrayList<node> way = new ArrayList<>();
    node begin, last;

    void dfs() {
        open.add(begin);
        while (!open.isEmpty() /*&& i < 30000*/) {
            System.out.println(close.size());
            System.out.println(i);
            i++;
            node on = open.pop();
            close.add(on);
            if (on.equals(last)) {
                found = true;
                last.parent = on.parent;
                return;
            } else {
                on.makechildren();
                for (int j = 0; j < on.child.size(); j++) {
                    if (!close.contains(on.child.get(j))) {
                        open.add(on.child.get(j));
                    }
                }
            }
        }
        System.out.println("not found!");
    }

    String desidetypr() {
        if (way.get(go).emptyX == way.get(go + 1).emptyX - 1)
            return "LEFT";
        else if (way.get(go).emptyX == way.get(go + 1).emptyX + 1)
            return "RIGHT";
        else if (way.get(go).emptyY == way.get(go + 1).emptyY - 1)
            return "UP";
        else if (way.get(go).emptyY == way.get(go + 1).emptyY + 1)
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
            dfs();
            if (found) {
                node temp = last;
                way.add(temp);
                while (temp.parent != null) {
                    way.add(temp);
                    temp = temp.parent;
                }
                way.add(begin);
                go = way.size() - 2;
                return desidetypr();
            }
            return null;
        } else {
            go--;
            return desidetypr();
        }
    }
}
