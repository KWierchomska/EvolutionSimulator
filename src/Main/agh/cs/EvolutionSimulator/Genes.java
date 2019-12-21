package agh.cs.EvolutionSimulator;

import java.util.Arrays;
import java.util.Random;

public class Genes {
    private static final int NUMBEROFGENES=32;
    private static final int TYPES=8;
    private int [] setOfGenes = new int [NUMBEROFGENES];

    public Genes(){
        this.fillRandomGene();
        this.fixGene();
    }

    public Genes(Genes mother, Genes father){
        this.generateGenes(mother,father);
        this.fixGene();
    }

    private void generateGenes(Genes mother, Genes father) {
        int firstDivision= new Random().nextInt(NUMBEROFGENES ) ;
        int secondDivision = firstDivision;
        while(firstDivision==secondDivision){
            secondDivision=new Random().nextInt(NUMBEROFGENES) ;
        }
        if (firstDivision > secondDivision){ //check if second part is further, if not, swap parts
            int tmp = secondDivision;
            secondDivision = firstDivision;
            firstDivision = tmp;
        }
        System.arraycopy(mother.setOfGenes, 0, this.setOfGenes, 0, firstDivision );
        System.arraycopy(father.setOfGenes, firstDivision , this.setOfGenes, firstDivision , secondDivision - firstDivision);
        System.arraycopy(mother.setOfGenes, secondDivision, this.setOfGenes, secondDivision, NUMBEROFGENES - secondDivision);
        Arrays.sort(this.setOfGenes);
    }

    private void fixGene(){
        int [] typesOfGenes = new int[8];
        for (int i=0; i<32; i++) {
            typesOfGenes[this.setOfGenes[i]] += 1;
        }
        for (int i=0; i<TYPES; i++){
            if (typesOfGenes[i] <= 0){ // check which gen is missing
                int j = i;
                while(typesOfGenes[this.setOfGenes[j]] < 1) j++; // check if by adding missing gene we are not removing another
                this.setOfGenes[j]=i;
            }
        }
        Arrays.sort(this.setOfGenes);
    }

    private void fillRandomGene() {
        for(int i=0; i<NUMBEROFGENES; i++){
            this.setOfGenes[i]=new Random().nextInt(TYPES);
        }
        Arrays.sort(this.setOfGenes);
    }

    public int[] getSetOfGenes() {
        return this.setOfGenes;
    }
}
