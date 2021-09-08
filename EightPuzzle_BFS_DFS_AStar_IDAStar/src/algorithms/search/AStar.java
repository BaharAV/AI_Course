package algorithms.search;

import algorithms.Algorithm;
import game.node;

import java.util.*;

public class AStar implements Algorithm {
    int call = 0;
    int go;
    int i = 0;
    boolean found = false;
    PriorityQueue<node> open = new PriorityQueue<>(new nodecomp());
    LinkedList<node> close = new LinkedList<>();
    ArrayList<node> way = new ArrayList<>();
    node begin, last;

    void astar() {
        way = new ArrayList<>();
        begin.g = 0;
        begin.manhatan();
        begin.findf();
        open.add(begin);
        while (!open.isEmpty()) {
//            System.out.println(i);
//            i++;
            node u = open.poll();
            close.add(u);
            u.makechildren();
            for (int i = 0; i < u.child.size(); i++) {
                u.child.get(i).g = u.g + 1;
                u.child.get(i).findf();
                if (!close.contains(u.child.get(i))) {
                    open.add(u.child.get(i));
                    if (last.equals(u.child.get(i))) {
                        last.parent = u;
                        last.manhatan();
                        last.findf();
                        return;
                    }
                } else {
                    int index = close.indexOf(u.child.get(i));
                    if (close.get(index).f > u.child.get(i).f) {
                        close.get(index).g = u.child.get(i).g;
                        close.get(index).f = u.child.get(i).f;
                        close.get(index).parent = u;
                    }
                }
            }
        }
    }


    class nodecomp implements Comparator<node> {
        public int compare(node n1, node n2) {
            if (n1.f > n2.f)
                return 1;
            else if (n1.f < n2.f)
                return -1;
            return 0;
        }
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
            astar();
            node temp = last;
            way.add(temp);
            while (temp.parent != null) {
                way.add(temp);
                temp = temp.parent;
            }
            way.add(begin);
            go = way.size() - 2;
            return desidetypr();
        } else {
            go--;
            return desidetypr();
        }
    }
}
