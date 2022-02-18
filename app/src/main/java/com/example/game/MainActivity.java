package com.example.game;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    GridLayout grid;
    TextView textID;
    Button startButton;
    boolean start = false;
    int[][] matriz2048;
    int[][] mAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matriz2048 = new int[4][4];
        grid = findViewById(R.id.grid);
        startButton = findViewById(R.id.button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!start) {
                    start = true;
                } else {
                    iniciarMatriz(matriz2048);
                }
                generateNumber(matriz2048);
                updateGrid(matriz2048);
            }
        });

        //controlador de gestos
        grid.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                System.out.println("Swipe Up");
                if(start) {
                    updateUp(matriz2048);
                    generateNumber(matriz2048);
                    updateGrid(matriz2048);
                    if (!checkZero(matriz2048)) {
                        if(checkAdd(matriz2048)) {
                            System.out.println("Lose");
                            start = false;
                        }
                    }
                }
            }
            public void onSwipeRight() {
                System.out.println("Swipe Right");
                if (start) {
                    updateRight(matriz2048);
                    generateNumber(matriz2048);
                    updateGrid(matriz2048);
                    if (!checkZero(matriz2048)) {
                        if(checkAdd(matriz2048)) {
                            System.out.println("Lose");
                            start = false;
                        }
                    }
                }
            }
            public void onSwipeLeft() {
                System.out.println("Swipe Left");
                if (start) {
                    updateLeft(matriz2048);
                    generateNumber(matriz2048);
                    updateGrid(matriz2048);
                    if (!checkZero(matriz2048)) {
                        if(checkAdd(matriz2048)) {
                            System.out.println("Lose");
                            start = false;
                        }
                    }
                }
            }
            public void onSwipeBottom() {
                System.out.println("Swipe Down");
                if (start) {
                    updateDown(matriz2048);
                    generateNumber(matriz2048);
                    updateGrid(matriz2048);
                    if (!checkZero(matriz2048)) {
                        if(checkAdd(matriz2048)) {
                            System.out.println("Lose");
                            start = false;
                        }
                    }
                }
            }
        });
    }

    public TextView getTextViewID(int i, int j) {
        String viewID = "view"+i+j;
        int resID = getResources().getIdentifier(viewID, "id", getPackageName());
        return findViewById(resID);
    }

    public void iniciarMatriz(int[][] m) {
        System.out.println("Matrix Created");
        for (int i=0; i<m.length; i++) {
            for (int j=0; j<m[0].length; j++) {
                m[i][j] = 0;
            }
        }
    }

    public void updateGrid(int[][] m) {
        System.out.println("Grid updated");
        for (int i=0; i<m.length; i++) {
            for (int j=0; j<m[0].length; j++) {
                textID = getTextViewID(i, j);
                if (matriz2048[i][j] != 0) {
                    textID.setText(Integer.toString(m[i][j]));
                } else {
                    textID.setText(getString(R.string.null_value));
                }
            }
        }
    }

    public boolean checkZero(int[][] m) {
        for(int i = 0; i<m.length; i++) {
            for(int j=0; j<m[0].length; j++) {
                if (m[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkAdd(int[][] m) {
        int[][] mAux = new int[4][4];
        copyMatriz(m, mAux);
        updateDown(mAux);
        if(!matrizIgual(mAux, m)) {
            return false;
        }
        copyMatriz(m, mAux);
        updateUp(mAux);
        if(!matrizIgual(mAux, m)) {
            return false;
        }
        copyMatriz(m, mAux);
        updateLeft(mAux);
        if(!matrizIgual(mAux, m)) {
            return false;
        }
        copyMatriz(m, mAux);
        updateRight(mAux);
        if(!matrizIgual(mAux, m)) {
            return false;
        }
        return true;
    }

    public void copyMatriz(int[][] m, int[][] n) {
        for (int i=0; i<m.length; i++) {
            for (int j = 0; j< m[0].length; j++) {
                n[i][j] = m[i][j];
            }
        }
    }

    public boolean matrizIgual(int[][] m, int[][] n) {
        for(int i=0; i<m.length; i++) {
            for (int j= 0; j<m[0].length; j++) {
                if (m[i][j] != n[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    //genera la oportunidad de clocar un 2 en una casilla de la matriz grid que contenga un 0
    public void generateNumber(int[][] m) {
        boolean run = true;
        if (checkZero(m)) {
            while (run) {
                int r1 = (int) (Math.random() * 4);
                int r2 = (int) (Math.random() * 4);
                if (m[r1][r2] == 0) {
                    run = false;
                    int r = (int) (Math.random() * 10+1);
                    if (r == 1) {
                        m[r1][r2] = 4;
                    } else {
                        m[r1][r2] = 2;
                    }
                }
            }
        }
    }

    public void updateUp(int[][] m) {
        for (int col = 0; col<m.length; col++) {
            movementUp(col, m);
            addUp(col, m);
            movementUp(col, m);
        }
    }

    public void movementUp(int col, int[][] m) {
        for (int i=0; i<3; i++) {
            for (int fil = 0; fil < m.length - 1; fil++) {
                if (m[fil][col] == 0 && !(m[fil+1][col] == 0)) {
                    m[fil][col] = m[fil+1][col];
                    m[fil+1][col] = 0;
                }
            }
        }
    }

    public void addUp(int col, int[][] m) {
        for (int fil = 0; fil<m.length-1; fil++) {
            if (m[fil][col] == m[fil+1][col]) {
                int aux = m[fil][col] + m[fil+1][col];
                m[fil][col] = aux;
                m[fil+1][col] = 0;
            }
        }
    }

    public void updateDown(int[][] m) {
        for (int col = 0; col<m.length; col++) {
            movementDown(col, m);
            addDown(col, m);
            movementDown(col, m);
        }
    }

    public void movementDown(int col, int[][] m) {
        for (int i=0; i<3; i++) {
            for (int fil = m.length-1; fil > 0; fil--) {
                if (m[fil][col] == 0 && !(m[fil-1][col] == 0)) {
                    m[fil][col] = m[fil-1][col];
                    m[fil-1][col] = 0;
                }
            }
        }
    }

    public void addDown(int col, int[][] m) {
        for (int fil = m.length-1; fil>0; fil--) {
            if (m[fil][col] == m[fil-1][col]) {
                int aux = m[fil][col] + m[fil-1][col];
                m[fil][col] = aux;
                m[fil-1][col] = 0;
            }
        }
    }

    public void updateLeft(int[][] m) {
        for (int fil = 0; fil<m.length; fil++) {
            movementLeft(fil, m);
            addLeft(fil, m);
            movementLeft(fil, m);
        }
    }

    public void movementLeft(int fil, int[][] m) {
        for (int i=0; i<3; i++) {
            for (int col = 0; col <m.length-1; col++) {
                if (m[fil][col] == 0 && !(m[fil][col+1] == 0)) {
                    m[fil][col] = m[fil][col+1];
                    m[fil][col+1] = 0;
                }
            }
        }
    }

    public void addLeft(int fil, int[][] m) {
        for (int col = 0; col<grid.getColumnCount()-1; col++) {
            if (m[fil][col] == m[fil][col+1]) {
                int aux = m[fil][col] + m[fil][col+1];
                m[fil][col] = aux;
                m[fil][col+1] = 0;
            }
        }
    }

    public void updateRight(int[][] m) {
        for (int fil = 0; fil<m.length; fil++) {
            movementRight(fil, m);
            addRight(fil, m);
            movementRight(fil, m);
        }
    }

    public void movementRight(int fil, int[][] m) {
        for (int i=0; i<3; i++) {
            for (int col = m.length-1; col >0; col--) {
                if (m[fil][col] == 0 && !(m[fil][col-1] == 0)) {
                    m[fil][col] = m[fil][col-1];
                    m[fil][col-1] = 0;
                }
            }
        }
    }

    public void addRight(int fil, int[][] m) {
        for (int col = m.length-1; col>0; col--) {
            if (m[fil][col] == m[fil][col-1]) {
                int aux = m[fil][col] + m[fil][col-1];
                m[fil][col] = aux;
                m[fil][col-1] = 0;
            }
        }
    }

}