package agh.cs.EvolutionSimulator;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class GenesTest {
    public Genes mother=new Genes();
    public Genes father=new Genes();
    public Genes child=new Genes(mother,father);

    @Test
    public void testGenes(){
        int [] typeOfGenesMother= IntStream.rangeClosed(0,7).toArray();
        int [] typeOfGenesFather= IntStream.rangeClosed(0,7).toArray();
        int [] typeOfGenesChild= IntStream.rangeClosed(0,7).toArray();
        for(int i=0; i< 32; i++){
            typeOfGenesMother[mother.getSetOfGenes()[i]]+=1;
            typeOfGenesFather[father.getSetOfGenes()[i]]+=1;
            typeOfGenesChild[child.getSetOfGenes()[i]]+=1;
        }
        for(int i=0; i<8; i++){
            assertTrue(typeOfGenesFather[i] > 0 && typeOfGenesMother[i] > 0 && typeOfGenesChild[i] >0 );
        }
    }


}