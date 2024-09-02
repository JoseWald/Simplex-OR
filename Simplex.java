import java.util.ArrayList;
import java.util.List;

public class Simplex {
        private List<Float> equation;
        private final boolean maximize; // true: max, false: min
        private List<List<Float>> constraint;
        // 0:<; 1:<= ; 2:= ; 3:> ; 4:>=
        private final short[] constInegality; // array of inequality signs
        private  float[] solution; 
        private int[] Xb; // base's index
        private int[] Xn;//non base's index
        private float base[]; // base

        Simplex(float[] equation, boolean maximize, float[][] constraint, short[] constInegality,float base[]) {
            this.equation=new ArrayList<>();
            for(float value : equation){
                this.equation.add(value);
            }
            this.maximize = maximize;

            this.Xn=new int[equation.length];
            
            this.constraint=new ArrayList<>();
            for(float[] row:constraint){
                List<Float> listRow= new ArrayList<>();
                for(float value:row){
                    listRow.add(value);
                }
                this.constraint.add(listRow);
            }
            this.constInegality = constInegality;

            this.Xb = new int[constraint.length];

            this.solution = new float[base.length];

            this.base=base;
        }
        // Method to put it in standard form
     
        private void standardInit() {

            for (int i = 0; i < constraint.size(); i++) {

                for(int k=0 ; k<i ; k++){
                    constraint.get(i).add(0f);
                    equation.add(0f);
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
            if(!maximize) {
                for(int i = 0; i < equation.size(); i++) {
                    equation.set(i,-equation.get(i));
                }
            }
             int j=0;
            for(int i=equation.indexOf(0.0f); i<equation.size(); i++) {
                Xb[j]=i;
                j++;
            }

            for(int i=0; i<equation.size(); i++) {
                    if(equation.get(i)==0.0f)break;
                    Xn[i]= i;        
            }
    }
    private void dispConstr(){
        System.out.println(equation);
         int i=0;
        for (List<Float> row : constraint) {
            System.out.println(row +""+this.base[i]);
            i++;
        }
        System.out.print("Xb:");
        for(int j=0; j<Xb.length; j++){
            System.out.print(Xb[j]);
        }
        System.out.println("");
        System.out.print("Xn:");
        for(int j=0; j<Xn.length; j++){
            System.out.print(Xn[j]);
        }
        System.out.println("");

        
    }

    //the base matrix is already an identity matrix,so there is no need to invert it
    //the simplex multiplicator will be always filled by 0
    //let's just handle the Cn (non base's cost)
    private float[] Cn(){
       // if all Cn's value >=0 ,we found the optimum
        float solution[]=new float[Xb.length];
        int i=0;
        for( i=0; i<Xn.length ; i++){
            if(Xn[i]<0)break;
        }
        //optimum found
        if(i==Xn.length-1){
                for(int j=0 ; j<Xb.length ; j++){
                    solution[j]=Xb[j];
                }
           
        }
        return solution;

    }

    private void setXn(){
        //Xn[]:non base's index
        int min=Xn[0];
        for(int i=1;i<Xn.length ; i++){
            if(equation.get(Xn[i])<equation.get(min)){
                min=i;
            }
        }
        System.err.println(equation.get(min)+" entre en base");

         Xn[min]=setXb(min);

         //min:indexOf pivot row
         pivot(min, min);

    }

    private int setXb(int newBaseIndex){
        float Min[]=new float[base.length];
        for(int i=0 ; i<base.length; i++){
            Min[i]=base[i]/constraint.get(i).get(newBaseIndex);
        }
        int  outOfBaseInd=0;
        for(int i=1 ; i<Min.length; i++){
            if(Min[i]<Min[outOfBaseInd] && Min[outOfBaseInd]>=0){
                outOfBaseInd=i;
            }
        }
        System.err.println(equation.get(outOfBaseInd)+ "sort de la base");

        Xn[newBaseIndex]=outOfBaseInd;
        return outOfBaseInd;

    }

    //pRowInd:pivot row index
    private void pivot(int pRowInd,int newBaseIndex){

        float L;
        L=-equation.get(newBaseIndex)/constraint.get(pRowInd).get(newBaseIndex);
        for(int i=0; i<equation.size(); i++){
            equation.set(i, equation.get(i)+constraint.get(pRowInd).get(i)*L);
        }

        L=1/constraint.get(0).get(newBaseIndex);
        for(int i=0; i<constraint.get(0).size(); i++){
           constraint.get(0).set(i,constraint.get(0).get(i)*L);
        }
        base[pRowInd-1]*=L;

        for(int i=1 ; i<constraint.size(); i++){
            L=constraint.get(i).get(newBaseIndex)/constraint.get(pRowInd).get(newBaseIndex);
            for(int j = 0; j<constraint.get(0).size(); j++){
                 constraint.get(i).set(j,constraint.get(i).get(j)*L);
            }
        }

    }

    public void resolution(){
        standardInit();
        while (this.solution==null) {
            this.solution=Cn();
            setXn();
            dispConstr();
        }
        System.out.println("Solution: " + this.solution[0]+" " + this.solution[1]);

    }
   
    public static void main(String[] args){
        // Sample data for testing
        float[] equation = {2f, 4f};
        boolean maximize = true;
        float[][] constraints = {
            {3f, 4f},  
            {2f, 5f},
      
        };
        short[] inequalitySigns = {1, 1 }; 

        float base[]={1700f , 1600f };

        Simplex simplex = new Simplex(equation, maximize, constraints, inequalitySigns,base);

        simplex.resolution();
       
      
    }
}
