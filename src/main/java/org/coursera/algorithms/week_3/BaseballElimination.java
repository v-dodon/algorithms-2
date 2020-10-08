package org.coursera.algorithms.week_3;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {
    private final Map<String, Team> teams;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        teams = new HashMap<>();
        int teamNo = 0;
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] lineSplit = line.trim().split("\\s+");
            if (line.isEmpty() || lineSplit.length == 1) {
                continue;
            }
            String teamName = lineSplit[0];
            int wins = Integer.parseInt(lineSplit[1]);
            int losses = Integer.parseInt(lineSplit[2]);
            int remaining = Integer.parseInt(lineSplit[3]);
            int[] against = new int[lineSplit.length - 4];
            for (int i = 4; i < lineSplit.length; i++) {
                int index = i - 4;
                against[index] = Integer.parseInt(lineSplit[i]);
            }
            teams.put(teamName, new Team(teamNo, wins, losses, remaining, against, teamName));
            teamNo++;
        }
    }                    // create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return teams.size();
    }            // number of teams

    public Iterable<String> teams() {
        return teams.keySet();
    }                                // all teams

    public int wins(String team) {
        checkTeam(team);
        return teams.get(team).wins;
    }             // number of wins for given team

    public int losses(String team) {
        checkTeam(team);
        return teams.get(team).losses;
    }                // number of losses for given team

    public int remaining(String team) {
        checkTeam(team);
        return teams.get(team).remaining;
    }              // number of remaining games for given team

    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        int[] against = teams.get(team1).against;
        int team2Id = teams.get(team2).id;
        return against[team2Id];
    }   // number of remaining games between team1 and team2

    public boolean isEliminated(String team) {
        checkTeam(team);
        Team searchedTeam = teams.get(team);
        int wins = searchedTeam.wins + searchedTeam.remaining;
        Collection<Team> values = teams.values();
        for (Team other : values) {
            if (wins < other.wins) {
                searchedTeam.isTrivialEliminated = true;
                return true;
            }
        }

        FordFulkerson fordFulkerson = computeFordFulkerson(searchedTeam, values);
        for (int i = 0; i < teams.size(); i++) {
            if (fordFulkerson.inCut(i)) {
                return true;
            }
        }
        return false;
    }       // is given team eliminated?

    private FordFulkerson computeFordFulkerson(Team searchedTeam, Collection<Team> values) {
        int[] against = searchedTeam.against;
        int vertices = against.length - 1;
        int id = searchedTeam.id;
        for (int i = against.length - 2; i > 0; i--) {
            vertices += i;
        }
        vertices = vertices + 3;
        int source = vertices - 2;
        int target = vertices - 1;
        int gameIndex = source - 1;
        FlowNetwork flowNetwork = new FlowNetwork(vertices);
        boolean[] marked = new boolean[against.length];
        for (int i = 0; i < values.size(); i++) {
            if (gameIndex == against.length - 1) {
                break;
            }
            for (Team t : values) {
                int gamesLeft = t.against[i];
                if (t.id == id || t.id == i || searchedTeam.id == i || marked[t.id]) {
                    continue;
                }
                if (gameIndex == against.length - 1) {
                    break;
                }
                FlowEdge flowEdge = new FlowEdge(source, gameIndex, gamesLeft);
                FlowEdge teamEdge = new FlowEdge(gameIndex, t.id, Double.POSITIVE_INFINITY);
                FlowEdge otherTeamEdge = new FlowEdge(gameIndex, i, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge);
                flowNetwork.addEdge(teamEdge);
                flowNetwork.addEdge(otherTeamEdge);
                gameIndex--;
            }
            marked[i] = true;
        }

        for (Team t : values) {
            if (t.id == searchedTeam.id) {
                continue;
            }
            int capacity = searchedTeam.wins + searchedTeam.remaining - t.wins;
            FlowEdge flowEdge = new FlowEdge(t.id, target, capacity);
            flowNetwork.addEdge(flowEdge);
        }
        return new FordFulkerson(flowNetwork, source, target);
    }

    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        if (!isEliminated(team)) {
            return null;
        }
        Team searchedTeam = teams.get(team);
        Stack<String> stack = new Stack<>();
        if (searchedTeam.isTrivialEliminated) {
            fillTrivialEliminated(searchedTeam, stack);
        } else {
            fillNonTrivialEliminated(searchedTeam, stack);
        }
        return stack;
    }  // subset R of teams that eliminates given team; null if not eliminated

    private void fillNonTrivialEliminated(Team searchedTeam, Stack<String> stack) {
        FordFulkerson fordFulkerson = computeFordFulkerson(searchedTeam, teams.values());
        for (Team team : teams.values()) {
            if (searchedTeam.id != team.id && fordFulkerson.inCut(team.id)) {
                stack.push(team.name);
            }
        }
    }

    private void fillTrivialEliminated(Team searchedTeam, Stack<String> stack) {
        int wins = searchedTeam.wins + searchedTeam.remaining;
        for (Team team : teams.values()) {
            if (searchedTeam.id != team.id && wins < team.wins) {
                stack.push(team.name);
            }
        }
    }

    private void checkTeam(String team) {
        if (teams.get(team) == null) {
            throw new IllegalArgumentException();
        }
    }

    private class Team {
        private final int id;
        private final int wins;
        private final int losses;
        private final int remaining;
        private boolean isTrivialEliminated;
        private final int[] against;
        private final String name;

        private Team(int id, int wins, int losses, int remaining, int[] against, String name) {
            this.id = id;
            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
            this.against = against;
            this.name = name;
        }
    }
}
