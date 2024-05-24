import java.util.*;

public class Automaton {
    private Set<State> states;
    private State initialState;
    private Set<State> finalStates;
    private Map<State, Map<Character, Set<State>>> transitions;
    public static final char EPSILON = '\u03B5'; // Unicode pour le symbole epsilon

    public Automaton(Set<State> states, Set<Character> alphabet, State initialState, Set<State> finalStates) {
        this.states = states;
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.transitions = new HashMap<>();
    }

    public Automaton(Set<State> dfaStates, Set<Character> alphabet, State initialState, Set<State> dfaFinalStates, Map<Set<State>, Map<Character, Set<State>>> dfaTransitions) {
        this.states = dfaStates;
        this.initialState = initialState;
        this.finalStates = dfaFinalStates;
        this.transitions = new HashMap<>();

        // Convertir dfaTransitions en la structure attendue par transitions
        for (Map.Entry<Set<State>, Map<Character, Set<State>>> entry : dfaTransitions.entrySet()) {
            Set<State> fromStates = entry.getKey();
            Map<Character, Set<State>> symbolTransitions = entry.getValue();
            for (State fromState : fromStates) {
                transitions.put(fromState, symbolTransitions);
            }
        }
    }// Getters and setters

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(Set<State> finalStates) {
        this.finalStates = finalStates;
    }

    public Map<State, Map<Character, Set<State>>> getTransitions() {
        return transitions;
    }

    public void addTransition(State fromState, char symbol, Set<State> toStates) {
        transitions.computeIfAbsent(fromState, k -> new HashMap<>());
        transitions.get(fromState).put(symbol, toStates);
    }

    // Méthode pour afficher les transitions
    private String transitionsToString() {
        StringBuilder sb = new StringBuilder();
        for (State state : transitions.keySet()) {
            for (Map.Entry<Character, Set<State>> entry : transitions.get(state).entrySet()) {
                char symbol = entry.getKey();
                Set<State> toStates = entry.getValue();
                sb.append(state).append(" --").append(symbol).append("--> ").append(statesToString(toStates)).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Automaton{\n");
        sb.append("states=").append(states).append("\n");
        sb.append("initialState=").append(initialState).append("\n");
        sb.append("finalStates=").append(finalStates).append("\n");
        sb.append("transitions=\n").append(transitionsToString());
        sb.append('}');
        return sb.toString();
    }
    
    // Méthode locale pour formater les états
    private String statesToString(Set<State> states) {
        StringBuilder sb = new StringBuilder();
        for (State state : states) {
            sb.append(state.getName()).append(" ");
        }
        return sb.toString().trim();
    }
    
    public Set<Character> getAlphabet() {
        Set<Character> alphabet = new HashSet<>();
        for (Map<Character, Set<State>> transition : transitions.values()) {
            alphabet.addAll(transition.keySet());
        }
        return alphabet;
    }
}
