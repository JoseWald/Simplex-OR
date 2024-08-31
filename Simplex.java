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
            this.equation=equation;
            this.maximize = maximize;
            
            this.constraint=new ArrayList<>();
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

                for(int k=0 ; k<i ; k++){
                    constraint.get(i).add(0f);
                }

                if (constInegality[i] == 0 || constInegality[i] == 1) {
                    constraint.get(i).add(1f);
                    for(int j=0 ; j<constraint.size() -i-1 ; j++) {
                        constraint.get(i).add(0f);
                    }
                }else if(constInegality[i] == 3 || constInegality[i] == 4) {
                    constraint.get(i).add(-1f);
                    for(int j=0 ; j<constraint.size() -i -1; j++) {
                        constraint.get(i).add(0f);
                    }

                }
                
            }
    }

    public static void main(String[] args){
        // Sample data for testing
        float[] equation = {1f, 2f};
        boolean maximize = true;
        float[][] constraints = {
            {2f, 1f},  
            {1f, 1f},
            {2f,2f}  
        };
        short[] inequalitySigns = {1, 1 , 1}; 

        Simplex simplex = new Simplex(equation, maximize, constraints, inequalitySigns);
        
        simplex.standardInit();
        
        // Print the constraints to verify the result
        for (List<Float> row : simplex.constraint) {
            System.out.println(row);
        }
    }
}
