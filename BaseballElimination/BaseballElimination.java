/* *****************************************************************************
 *  Name: Bisrat Zerihun
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.LinkedList;

public class BaseballElimination {
    private int[] w;
    private int[] r;
    private int[] l;
    private int[][] g;
    private int teamCount;
    private HashMap<String, Integer> teams;
    private HashMap<Integer, String> teamsToId;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In reader = new In(filename);
        teamCount = reader.readInt();
        w = new int[teamCount];
        r = new int[teamCount];
        l = new int[teamCount];
        g = new int[teamCount][teamCount];
        teams = new HashMap<>();
        teamsToId = new HashMap<>();
        processFile(reader);
    }

    // number of teams
    public int numberOfTeams() {
        return teamCount;
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        int index = teams.get(team);
        return w[index];
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        int index = teams.get(team);
        return l[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        int index = teams.get(team);
        return r[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1,
                       String team2) {
        validateTeam(team1);
        validateTeam(team2);
        int index1 = teams.get(team1);
        int index2 = teams.get(team2);
        return g[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);
        int index = teams.get(team);
        if (trivialEliminated(index).size() == 0) {
            return nonTrivialEliminated(index).size() > 0;
        }
        return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(
            String team) {
        validateTeam(team);
        LinkedList<String> listOfTeams = trivialEliminated(teams.get(team));
        if (listOfTeams.size() > 0) {
            return listOfTeams;
        }
        else if ((listOfTeams = nonTrivialEliminated(teams.get(team))).size() > 0) {
            return listOfTeams;
        }
        else return null;
    }

    private void processFile(In reader) {
        for (int i = 0; i < teamCount; i++) {
            String teamName = reader.readString();
            teams.put(teamName, i);
            teamsToId.put(i, teamName);
            w[i] = reader.readInt();
            l[i] = reader.readInt();
            r[i] = reader.readInt();
            for (int j = 0; j < teamCount; j++) {
                if (j != i)
                    g[i][j] = reader.readInt();
                else {
                    reader.readInt();
                    g[i][j] = 0;
                }

            }

        }
    }

    private void validateTeam(String team) {
        if (team == null || !teams.containsKey(team))
            throw new IllegalArgumentException("null parameter");
    }

    private LinkedList<String> trivialEliminated(int index) {
        LinkedList<String> eliminators = new LinkedList<>();
        for (int i = 0; i < teamCount; i++) {
            if (w[index] + r[index] < w[i]) {
                eliminators.add(teamsToId.get(i));
            }
        }
        return eliminators;
    }

    private LinkedList<String> nonTrivialEliminated(int index) {
        LinkedList<String> eliminators = new LinkedList<>();
        FlowNetwork network = createNetwork(index);
        int matches = teamCount * (teamCount - 1) / 2;
        FordFulkerson traverse = new FordFulkerson(network, 0, matches + teamCount + 1);
        for (int i = 0; i < teamCount; i++) {
            if (traverse.inCut(i + matches + 1)) {
                String teamName = teamsToId.get(i);
                if (!eliminators.contains(teamName)) {
                    eliminators.add(teamName);
                }

            }
        }
        return eliminators;
    }

    private FlowNetwork createNetwork(int index) {
        int matches = teamCount * (teamCount - 1) / 2;
        FlowNetwork network = new FlowNetwork(matches + teamCount + 2);
        int gameIndex = 1;
        for (int i = 0; i < g.length; i++) {
            for (int j = i + 1; j < g[0].length; j++) {
                if (i != index && j != index && i != j) {
                    network.addEdge(new FlowEdge(0, gameIndex, g[i][j]));
                    network.addEdge(
                            new FlowEdge(gameIndex, matches + 1 + i, Double.POSITIVE_INFINITY));
                    network.addEdge(
                            new FlowEdge(gameIndex, matches + 1 + j, Double.POSITIVE_INFINITY));
                    gameIndex++;
                }
            }
            int cap = 0;
            if (w[index] + r[index] - w[i] > 0)
                cap = w[index] + r[index] - w[i];
            network.addEdge(new FlowEdge(matches + 1 + i, matches + teamCount + 1,
                                         cap));
        }
        return network;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
