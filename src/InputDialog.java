import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputDialog {
    BufferedReader br;
    InputDialog(){
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    public char[] inpNonTerminals() throws IOException {
        System.out.println("Enter the non-terminals");
        String nt = br.readLine();
        return nt.toCharArray();
    }
    public char[] inpTerminals() throws IOException {
        System.out.println("Enter the terminals");
        String t = br.readLine();
        return t.toCharArray();
    }
    public int readInt() throws IOException{
        return Integer.parseInt(br.readLine());
    }
    public String readLine() throws IOException{
        return br.readLine();
    }
}

