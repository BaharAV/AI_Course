package game;

import java.util.ArrayList;

public class node {
    public String[][] grid;
    public int emptyX, emptyY;
    public ArrayList<node> child = new ArrayList<>();
    public node parent;
    public int h, g, f;

    public node(String[][] grid, node parent) {
        this.grid = grid;
        this.findEmpty();
        this.parent = parent;
        this.manhatan();
    }

    @Override
    public boolean equals(Object obj) {
        node n = (node) obj;
        boolean is = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!n.grid[i][j].equals(this.grid[i][j])) {
                    is = false;
                    break;
                }
            }
        }
        return is;
    }

    public void manhatan() {
        String goal[][] = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                goal[i][j] = String.valueOf(i * 3 + j + 1);
            }
        }
        goal[2][2] = String.valueOf(0);
        int dif = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = j;
                int y = i;
                for (int k = 0; k < 3; k++) {
                    for (int m = 0; m < 3; m++) {
                        if (goal[i][j].equals(this.grid[k][m]) && !this.grid[i][j].equals("0")) {
                            int x2 = m;
                            int y2 = k;
                            dif = dif + (Math.abs(x2 - x) + Math.abs(y2 - y));
                        }
                    }
                }
            }
        }
        this.h = dif;
    }

    public void findf() {
        this.f = this.g + this.h;
    }

    public void findEmpty() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j].equals(" "))
                    updateEmptyCell(i, j);
            }
        }
    }

    public void updateEmptyCell(int i, int j) {
        this.emptyY = i;
        this.emptyX = j;
    }

    public void makechildren() {
        this.makechild(this.emptyY, this.emptyX + 1);
        this.makechild(this.emptyY, this.emptyX - 1);
        this.makechild(this.emptyY + 1, this.emptyX);
        this.makechild(this.emptyY - 1, this.emptyX);
    }

    public void makechild(int y, int x) {
        if (!isCellOutOfBounds(y, x)) {
            String[][] tempgrid = new String[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    tempgrid[i][j] = grid[i][j];
                }
            }
            tempgrid[y][x] = this.grid[this.emptyY][this.emptyX];
            tempgrid[this.emptyY][this.emptyX] = this.grid[y][x];
            this.child.add(new node(tempgrid, this));
        }
    }

    public boolean isCellOutOfBounds(int y, int x) {
        return y >= 3 || x >= 3 || y < 0 || x < 0;
    }
}

