import java.util.*;

public class DFATest {
    
    // Classe représentant un DFA
    static class DFA {
        int[] states; // Ensemble d'états
        int initialState; // État initial
        int[][] transitions; // Tableau de transitions
        Set<Integer> finalStates; // Ensemble d'états finaux
        
        // Constructeur du DFA
        public DFA(int[] states, int initialState, int[][] transitions, Set<Integer> finalStates) {
            this.states = states;
            this.initialState = initialState;
            this.transitions = transitions;
            this.finalStates = finalStates;
        }
        
        // Méthode pour vérifier si un mot est reconnu par le DFA
        public boolean recognize(String word) {
            int currentState = initialState; // Initialisation de l'état courant
            
            // Parcours de chaque symbole du mot
            for (char symbol : word.toCharArray()) {
                int symbolIndex = symbol - 'a'; // Conversion du symbole en indice (supposant des lettres minuscules)
                
                // Vérification de la transition pour le symbole actuel
                currentState = transitions[currentState][symbolIndex];
            }
            
            // Vérification si l'état final est atteint
            return finalStates.contains(currentState);
        }
    }
    
    public static void main(String[] args) {
        // Exemple d'un DFA représentant L = {ab, ba, bb}
        int[] states = {0, 1, 2}; // États {q0, q1, q2}
        int initialState = 0; // État initial q0
        int[][] transitions = {
            {1, -1}, // Transition de q0 pour 'a' à q1, pour 'b' à état non valide (-1)
            {-1, 2}, // Transition de q1 pour 'a' à état non valide (-1), pour 'b' à q2
            {-1, -1} // Transition de q2 pour tout symbole à état non valide (-1)
        };
        Set<Integer> finalStates = new HashSet<>(Arrays.asList(2)); // État final q2
        
        // Création du DFA
        DFA dfa = new DFA(states, initialState, transitions, finalStates);
        
        // Test de reconnaissance de mots
        String[] testWords = {"ab", "ba", "bb", "aa"};
        for (String word : testWords) {
            System.out.println("Le mot '" + word + "' est reconnu ? " + dfa.recognize(word));
        }
    }
}
