package algorithms.search;

import algorithms.Algorithm;
import game.node;

import java.util.*;

public class IDAStar implements Algorithm {
    int call = 0;
    int go;
    node begin, last;
    boolean found = false;
    public Stack<node> open;
    public List<node> close;
    private Set<Integer> costs = new HashSet<>();
    ArrayList<node> way = new ArrayList<>();

    void idastar() {
        begin.g = 0;
        begin.manhatan();
        begin.findf();
        int bound = begin.f;
        while (true) {
            idastardfs(begin, bound);
            if (found == true) {
                return;
            } else {
                bound = Collections.min(costs);
            }
            costs.clear();
        }
    }

    void idastardfs(node begin, int bound) {
        open = new Stack<>();
        open.add(begin);
        close = new ArrayList<>();
        while (!open.isEmpty()) {
            node u = open.pop();
            close.add(u);
            if (last.equals(u)) {
                found = true;
                last.parent = u;
                last.manhatan();
                last.findf();
                return;
            }
            if (u.f > bound) {
                costs.add(u.f);
            } else {
                u.makechildren();
                for (int i = 0; i < u.child.size(); i++) {
                    u.child.get(i).g = u.g + 1;
                    u.child.get(i).manhatan();
                    u.child.get(i).findf();
                    if (!close.contains(u.child.get(i))) {
                        open.push(u.child.get(i));
                    }
                }
            }
        }
        return;
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
            idastar();
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
