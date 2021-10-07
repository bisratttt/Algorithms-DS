/* *****************************************************************************
 *  Name:   Bisrat Zerihun
 *  Date:   07/26/2021
 *  Description:    a wordnet DS created from a list of nouns and their
 *                  relationship with other nouns
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {

    private Digraph wordnet;
    // maps each index with the string
    private HashMap<Integer, String> mapper;
    // contains the ids in which the noun appears
    private HashMap<String, Bag<Integer>> idContainer;
    private SAP shortPath;

    /**
     * creates the wordnet by taking the input files(synsets and hypernym links)
     *
     * @param synsets   file with nouns and their definiton and index
     * @param hypernyms relationship of hypernyms with nouns
     */
    public WordNet(String synsets, String hypernyms) {
        idContainer = new HashMap<String, Bag<Integer>>();
        mapper = new HashMap<Integer, String>();
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Argument is null");
        // processing the synsets
        In syn = new In(synsets);
        int count = 0;
        while (syn.hasNextLine()) {
            count++;
            String line = syn.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            // simple map of a synset with its id
            mapper.put(id, parts[1]);
            String[] nouns = parts[1].split(" ");
            // checking every synset noun and the points
            // it exists
            for (String n : nouns) {
                Bag<Integer> bag = idContainer.get(n);
                if (bag != null) {
                    bag.add(id);
                }
                else {
                    bag = new Bag<Integer>();
                    bag.add(id);
                    idContainer.put(n, bag);
                }
            }
        }
        // processing the hypernyms
        In hyp = new In(hypernyms);
        wordnet = new Digraph(count);
        while (hyp.hasNextLine()) {
            String hypId = hyp.readLine();
            String[] parts = hypId.split(",");
            // makes connection to hypernyms of a noun
            int v = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int w = Integer.parseInt(parts[i]);
                wordnet.addEdge(v, w);
            }
        }

        // check for rooted DAG (only one vertex as a root ancestor)
        DirectedCycle dc = new DirectedCycle(wordnet);
        shortPath = new SAP(wordnet);

        int rootNum = 0;
        for (int v = 0; v < count; v++) {
            if (wordnet.outdegree(v) == 0)
                rootNum++;
        }
        if (rootNum != 1 || dc.hasCycle())
            throw new IllegalArgumentException("Input has " + rootNum + " roots.");
    }


    /**
     * returns all nouns in the wordnet
     *
     * @return the nouns in the wordnet
     */
    public Iterable<String> nouns() {
        return idContainer.keySet();
    }

    /**
     * checks if a word is a noun
     *
     * @param word true if noun and false otherwise
     * @return true if noun and false otherwise
     */
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return idContainer.containsKey(word);
    }

    /**
     * distance between two nouns
     *
     * @param nounA first word
     * @param nounB second word
     * @return distance in int
     */
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (nounA.equals(nounB)) return 0;
        Bag<Integer> bag1 = idContainer.get(nounA);
        Bag<Integer> bag2 = idContainer.get(nounB);
        int dist = shortPath.length(bag1, bag2);
        return dist;
    }

    /**
     * a synset that is a common ancestor of nounA and nounB in the
     * shortest ancestral path
     *
     * @param nounA first noun
     * @param nounB second noun
     * @return the common ancestor(synset)
     */
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        int i = shortPath.ancestor(idContainer.get(nounA), idContainer.get(nounB));
        return mapper.get(i);
    }


    public static void main(String[] args) {
        // WordNet wn = new WordNet(args[0], args[1]);
        // String a = "a";
        // String b = "b";
        // StdOut.print(wn.distance(a, b));
    }
}
