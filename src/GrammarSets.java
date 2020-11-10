import java.io.IOException;

public class GrammarSets {
    private String grammar[][], first[], follow[];
    private InputDialog dialog;
    private char ntermnl[], termnl[];
    private int ntlen;
    private int callsCount;
    private String[] firstByTerm;
    private String[] followByTerm;
    private String[][] firstProduct;

    GrammarSets() {
        dialog = new InputDialog();
    }

    public void setNonTerminals() throws IOException {
        ntermnl = dialog.inpNonTerminals();
        ntlen = ntermnl.length;
        first = new String[ntlen];
        follow = new String[ntlen];
        firstByTerm = new String[ntlen];
        followByTerm = new String[ntlen];
        firstProduct = new String[ntlen][];
    }

    public void setTerminals() throws IOException {
        termnl = dialog.inpTerminals();
        //tlen = termnl.length;
    }

    public void setProductionRules() throws IOException {
        System.out.println("Specify the grammar(Enter 9 for epsilon production)");
        grammar = new String[ntlen][];
        for (int i = 0; i < ntlen; i++) {
            System.out.println("Enter the number of productions for " + ntermnl[i]);
            int n = 0;
            try {
                n = dialog.readInt();
            } catch (NumberFormatException nfe) {
                System.out.println("Non-numeric character entered. Terminating...");
                System.exit(10);
            }
            grammar[i] = new String[n];
            System.out.println("Enter the productions");
            for (int j = 0; j < n; j++)
                grammar[i][j] = dialog.readLine();
        }
    }

    public void dispFirst() {
        System.out.println("First Set");
        for (int i = 0; i < ntlen; i++) {
            first[i] = first(i);
            firstByTerm[i] = removeDuplicates(first[i]);
            System.out.println(firstByTerm[i]);
        }
    }

    public void dispSecond() {
        System.out.println("Follow Set");
        for (int i = 0; i < ntlen; i++) {
            follow[i] = follow(i);
            followByTerm[i] = removeDuplicates(follow[i]);
            System.out.println(followByTerm[i]);
        }
    }

    public boolean checkFirstFollow() {
        boolean cond1 = false, cond2 = false;
        for (int i = 0; i < firstByTerm.length; i++) {
            if (firstByTerm[i].contains("9"))
                cond1 = true;
            for (int j = 0; j < firstByTerm[i].length(); j = j + 2) {
                String ch = "" + firstByTerm[i].charAt(j);
                if (followByTerm[i].contains(ch)) {
                    cond2 = true;
                }
            }
            if (cond1 && cond2)
                return false;
        }
        return true;
    }

    String first(int i) {
        callsCount++;
        if (recDet()) {
            System.exit(100);
        }
        int found = 0;
        String temp = "", str = "";
        for (int j = 0; j < grammar[i].length; j++) //number of productions
        {
            if (recDet()) {
                System.exit(100);
            }
            for (int k = 0; k < grammar[i][j].length(); k++, found = 0) //when nonterminal has epsilon production
            {
                for (int l = 0; l < ntlen; l++) { //finding nonterminal
                    if (grammar[i][j].charAt(k) == ntermnl[l]) //for nonterminal in first set
                    {
                        str = first(l);
                        callsCount = 0;
                        if (!(str.length() == 1 && str.charAt(0) == '9')) { //when epsilon production is the only nonterminal production
                            temp = temp + str;

                        }
                        found = 1;
                        break;

                    }
                }
                callsCount++;
                if (found == 1) {
                    if (str.contains("9")) //here epsilon will lead to next nonterminal’s first set
                        continue;
                } else { //if first set includes terminal
                    temp = temp + grammar[i][j].charAt(k);
                }
                break;
            }
        }
        return temp;
    }

    String follow(int i) {
        char pro[], chr[];
        String temp = "";
        int j, k, l, m, n, found = 0;
        if (i == 0)
            temp = "$";
        for (j = 0; j < ntlen; j++) {
            for (k = 0; k < grammar[j].length; k++) //entering grammar matrix
            {
                pro = grammar[j][k].toCharArray();
                for (l = 0; l < pro.length; l++) //entering each production
                {
                    if (pro[l] == ntermnl[i]) //finding the nonterminal whose follow set is to be found
                    {
                        if (l == pro.length - 1) //if it is the last terminal/non-terminal then follow of current non-terminal
                        {
                            if (j < i)
                                temp = temp + follow[j];
                        } else {
                            for (m = 0; m < ntlen; m++) {
                                if (pro[l + 1] == ntermnl[m]) //first of next non-terminal otherwise (else later…)
                                {
                                    chr = first[m].toCharArray();
                                    for (n = 0; n < chr.length; n++) {
                                        if (chr[n] == '9') //if first includes epsilon
                                        {
                                            if (l + 1 == pro.length - 1)
                                                temp = temp + follow(j); //when non-terminal is second last
                                            else
                                                temp = temp + follow(m);
                                        } else
                                            temp = temp + chr[n]; //include whole first set except epsilon
                                    }
                                    found = 1;
                                }
                            }
                            if (found != 1)
                                temp = temp + pro[l + 1]; //follow set will include terminal(else is here)
                        }
                    }
                }
            }
        }
        return temp;
    }

    String removeDuplicates(String str) {
        int i;
        char ch;
        boolean seen[] = new boolean[256];
        StringBuilder sb = new StringBuilder(seen.length);
        for (i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (!seen[ch]) {
                seen[ch] = true;
                sb.append(ch);
                sb.append(',');
            }
        }
        if (sb.length() == 0)
            return "";
        else
            return sb.substring(0, sb.length() - 1);
        //return sb.toString();
    }

    private boolean recDet() {
        if (callsCount > ntlen+termnl.length) {
            System.out.println("Left recursion detected. Terminating...");
            return true;

        }
        return false;
    }
}
