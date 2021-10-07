/* *****************************************************************************
 *  Name:       Bisrat Zerihun
 *  Date:       07/31/2021
 *  Description:    Finds the outcast noun from a list of nouns considering
 *                  that all the input nouns are in the wordnet
 **************************************************************************** */

public class Outcast {
    private WordNet wordnet;

    /**
     * client constructor that takes wordnet as argument
     *
     * @param wordnet wordnet used to check outcast
     */
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    /**
     * identify the outcast
     *
     * @param nouns list of words to be checked
     * @return the outcast noun
     */
    public String outcast(String[] nouns) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < nouns.length; i++) {
            String noun = nouns[i];
            int tempSum = 0;
            for (String secondNoun : nouns) {
                tempSum += wordnet.distance(noun, secondNoun);
            }
            if (tempSum > max) {
                max = tempSum;
                index = i;
            }
        }
        return nouns[index];
    }

    public static void main(String[] args) {
        // WordNet wordnet = new WordNet(args[0], args[1]);
        // Outcast outcast = new Outcast(wordnet);
        // for (int t = 2; t < args.length; t++) {
        //     In in = new In(args[t]);
        //     String[] nouns = in.readAllStrings();
        //     StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        // }
    }
}
