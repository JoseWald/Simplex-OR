import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Simplex {
    private float equation[];
    private final boolean maximize; // true: max, false: min
    private List<List<Float>> constraint;
    // 0:<; 1:<= ; 2:= ; 3:> ; 4:>=
    private final short[] constInegality; // array of inequality signs
    private final float[] slack; // slack variables
    private short pivot;
    private short[] Xb; // base's index

    Simplex(float[] equation, boolean maximize, float[][] constraint, short[] constInegality) {
        this.equation = equation;
        this.maximize = maximize;
        
        for(float[] row:constraint){
            List<Float> listRow= new ArrayList<>();
            for(float value:row){
                listRow.add(value);
            }
            this.constraint.add(listRow);
        }
        this.constInegality = constInegality;

        this.slack = new float[constInegality.length];
        Arrays.fill(slack, 1);

        this.pivot = 2;

        this.Xb = new short[constraint.length];

    }

    // Method to put it in standard form
    private void standardInit() {
        for (int i = 0; i < constraint.size(); i++) {

            if(i>0)constraint.get(i).add(0f);

            if (constInegality[i] == 0 || constInegality[i] == 1) {
                constraint.get(i).add(1f);
                for(int j=0 ; j<constraint.size() -i ; j++) {
                    constraint.get(i).add(0f);
                }
            }else if(constInegality[i] == 3 || constInegality[i] == 4) {
                constraint.get(i).add(-1f);
                for(int j=0 ; j<constraint.size() -i ; j++) {
                    constraint.get(i).add(0f);
                }

            }
            // Add implementation for other inequality signs as needed
        }
    }

    public static void main(String[] args){
         Simplex simplex=new Simplex();
    }
}
