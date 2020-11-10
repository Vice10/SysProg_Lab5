import java.util.*;
import java.io.*;

public class Dispatcher {


    public static void main(String[] argv) throws IOException {
        GrammarSets gs = new GrammarSets();
        gs.setNonTerminals();
        gs.setTerminals();
        gs.setProductionRules();
        gs.dispFirst();
        gs.dispSecond();
        if (!gs.checkFirstFollow())
            System.out.println("FIRST/FOLLOW conflict detected");
    }
}