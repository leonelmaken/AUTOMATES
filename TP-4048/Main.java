import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Menu :");
            System.out.println("1. Créer un NFA");
            System.out.println("2. Créer un DFA à partir d'un NFA");
            System.out.println("3. Créer un DFA pour un langage donné");
            System.out.println("4. Quitter");
            System.out.print("Choix : ");
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    Automaton nfa = createNFA(scanner);
                    System.out.println("\nAutomate NFA créé :");
                    System.out.println(nfa);
                    break;
                case "2":
                    Automaton dfaFromNFA = createDFA(scanner);
                    System.out.println("\nAutomate DFA créé à partir du NFA :");
                    System.out.println(dfaFromNFA);
                    break;
                case "3":
                    Automaton dfaForLanguage = createDFAForLanguage(scanner);
                    System.out.println("\nAutomate DFA pour le langage donné :");
                    System.out.println(dfaForLanguage);
                    break;
                case "4":
                    System.out.println("Programme terminé.");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        } while (!choice.equals("4"));
        scanner.close();
    }

    // Méthode pour créer un NFA
    public static Automaton createNFA(Scanner scanner) {
        System.out.println("Création de l'automate NFA...");
        System.out.println("Exemple de saisie pour le nombre d'états : 3");
        System.out.println("Exemple de saisie pour les noms des états : q0 q1 q2");
        System.out.println("Exemple de saisie pour l'alphabet : a b");

        // Initialisation des ensembles d'états, de l'alphabet, de l'état initial et des états finaux
        Set<State> states = new HashSet<>();
        Set<Character> alphabet = new HashSet<>();
        State initialState = null;
        Set<State> finalStates = new HashSet<>();

        // Demander à l'utilisateur de saisir le nombre d'états
        System.out.print("Nombre d'états : ");
        int numStates = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne restante après la saisie du nombre d'états

        // Saisie des noms des états
        for (int i = 0; i < numStates; i++) {
            System.out.print("Nom de l'état " + (i + 1) + " : ");
            String stateName = scanner.nextLine();
            State state = new State(stateName);
            states.add(state);

            // Vérification de l'état initial et des états finaux
            System.out.print("Est-ce l'état initial ? (true/false) : ");
            String isInitialString = scanner.nextLine();
            boolean isInitial = Boolean.parseBoolean(isInitialString);
            if (isInitial) {
                initialState = state;
            }
            System.out.print("Est-ce un état final ? (true/false) : ");
            String isFinalString = scanner.nextLine();
            boolean isFinal = Boolean.parseBoolean(isFinalString);
            if (isFinal) {
                finalStates.add(state);
            }
        }

        // Saisie de l'alphabet
        System.out.print("Alphabet (séparé par des espaces) : ");
        String alphabetInput = scanner.nextLine();
        for (char symbol : alphabetInput.toCharArray()) {
            if (symbol != ' ') {
                alphabet.add(symbol);
            }
        }

        // Initialisation de l'automate NFA avec les données saisies
        Automaton nfa = new Automaton(states, alphabet, initialState, finalStates);

        // Saisie des transitions
        System.out.println("Saisie des transitions :");
        System.out.println("Exemple de transition pour l'état q0 avec le symbole a : q1 q2");
        for (State fromState : states) {
            for (Character symbol : alphabet) {
                System.out.print("Transition de l'état " + fromState.getName() + " pour le symbole '" + symbol + "' (séparés par des espaces) : ");
                String transitionInput = scanner.nextLine();
                String[] transitionStates = transitionInput.split(" ");
                Set<State> toStates = new HashSet<>();
                for (String stateName : transitionStates) {
                    State toState = new State(stateName);
                    toStates.add(toState);
                }
                nfa.addTransition(fromState, symbol, toStates);
            }
        }

        // Retourner l'automate NFA créé
        return nfa;
    }

    // Méthode pour créer un DFA à partir d'un NFA
    public static Automaton createDFA(Scanner scanner) {
        System.out.println("Création de l'automate DFA...");
        Automaton nfa = createNFA(scanner);
        Automaton dfa = convertToDFA(nfa);
        return dfa;
    }

    // Méthode pour créer un DFA pour un langage donné

    // Méthode pour vérifier si un automate est un NFA
    public static boolean isNFA(Automaton automaton) {
        for (State fromState : automaton.getTransitions().keySet()) {
            for (Map<Character, Set<State>> transitions : automaton.getTransitions().values()) {
                for (Set<State> toStates : transitions.values()) {
                    if (toStates.size() > 1) {
                        return true; // Si une transition a plusieurs états de destination, alors c'est un NFA
                    }
                }
            }
        }
        return false; // Sinon, c'est un DFA
    }
    public static Automaton createDFAForLanguage(Scanner scanner) {
        System.out.println("Création de l'automate DFA pour un langage donné...");
        System.out.print("Expression régulière : ");
        String regex = scanner.nextLine();
        
        // Analyse de l'expression régulière et construction de l'automate NFA
        Automaton nfa = createNFAFromRegex(regex);
        
        // Conversion de l'automate NFA en AFD
        Automaton dfa = convertToDFA(nfa);
        
        // Affichage de l'AFD généré
        System.out.println("\nAutomate DFA pour l'expression régulière '" + regex + "' :");
        System.out.println(dfa);
        
        return dfa;
    }
    
 private static Automaton createNFAFromRegex(String regex) {
    Stack<Automaton> stack = new Stack<>();

    for (char c : regex.toCharArray()) {
        switch (c) {
            case 'a':
            case 'b':
                // Pour les caractères 'a' et 'b', créez un automate simple avec une transition de l'état initial à un nouvel état final
                Automaton atom = new Automaton(
                    new HashSet<>(Arrays.asList(new State("q0"), new State("q1"))),
                    new HashSet<>(Collections.singletonList(c)),
                    new State("q0"),
                    new HashSet<>(Collections.singletonList(new State("q1")))
                );
                atom.addTransition(new State("q0"), c, Collections.singleton(new State("q1")));
                stack.push(atom);
                break;
            case '|':
                // Pour l'opérateur '|', fusionnez les deux automates au sommet de la pile en ajoutant des transitions epsilon
                Automaton b = stack.pop();
                Automaton a = stack.pop();
                State start = new State("q0");
                State end = new State("q3");
                a.addTransition(start, Automaton.EPSILON, Collections.singleton(a.getInitialState()));
                a.addTransition(start, Automaton.EPSILON, Collections.singleton(b.getInitialState()));
                for (State finalState : a.getFinalStates()) {
                    finalState.setFinal(false);
                    a.addTransition(finalState, Automaton.EPSILON, Collections.singleton(end));
                }
                for (State finalState : b.getFinalStates()) {
                    finalState.setFinal(false);
                    a.addTransition(finalState, Automaton.EPSILON, Collections.singleton(end));
                }
                a.setInitialState(start);
                a.setFinalStates(new HashSet<>(Collections.singletonList(end)));
                stack.push(a);
                break;
            case '*':
                // Pour l'opérateur '*', étoile de Kleene, modifiez l'automate au sommet de la pile pour inclure une boucle
                Automaton toStar = stack.pop();
                State newStart = new State("q0");
                State newEnd = new State("q1");
                toStar.addTransition(newStart, Automaton.EPSILON, Collections.singleton(toStar.getInitialState()));
                for (State finalState : toStar.getFinalStates()) {
                    finalState.setFinal(false);
                    toStar.addTransition(finalState, Automaton.EPSILON, Collections.singleton(toStar.getInitialState()));
                    toStar.addTransition(finalState, Automaton.EPSILON, Collections.singleton(newEnd));
                }
                toStar.setInitialState(newStart);
                toStar.addTransition(newStart, Automaton.EPSILON, Collections.singleton(newEnd));
                toStar.setFinalStates(new HashSet<>(Collections.singletonList(newEnd)));
                stack.push(toStar);
                break;
        }
    }

    // L'automate final est au sommet de la pile
    return stack.pop();
}

    public static Automaton convertToDFA(Automaton nfa) {
        Set<Character> alphabet = nfa.getAlphabet();
        // Initialiser les ensembles nécessaires pour la conversion
        Map<Set<State>, State> stateMapping = new HashMap<>();
        Queue<Set<State>> queue = new LinkedList<>();
        Set<State> dfaStates = new HashSet<>();
        Set<State> dfaFinalStates = new HashSet<>();
        Map<Set<State>, Map<Character, Set<State>>> dfaTransitions = new HashMap<>();

        // Initialisation
        State initialState = nfa.getInitialState();
        Set<State> initialSet = new HashSet<>();
        initialSet.add(initialState);
        queue.add(initialSet);
        stateMapping.put(initialSet, initialState);
        dfaStates.add(initialState);

        if (nfa.getFinalStates().contains(initialState)) {
            dfaFinalStates.add(initialState);
        }

        // Traitement de la file de nouveaux états
        while (!queue.isEmpty()) {
            Set<State> currentSet = queue.poll();
            State dfaState = stateMapping.get(currentSet);

            for (Character symbol : alphabet) {
                Set<State> newSet = new HashSet<>();
                for (State state : currentSet) {
                    Set<State> toStates = nfa.getTransitions().getOrDefault(state, Collections.emptyMap()).get(symbol);
                    if (toStates != null) {
                        newSet.addAll(toStates);
                    }
                }
                if (!newSet.isEmpty()) {
                    if (!stateMapping.containsKey(newSet)) {
                        State newState = new State(newSet.stream().map(State::getName).collect(Collectors.joining()));
                        stateMapping.put(newSet, newState);
                        queue.add(newSet);
                        dfaStates.add(newState);

                        if (nfa.getFinalStates().stream().anyMatch(newSet::contains)) {
                            dfaFinalStates.add(newState);
                        }
                    }
                    State toState = stateMapping.get(newSet);
                    dfaTransitions.computeIfAbsent(currentSet, k -> new HashMap<>()).put(symbol, newSet);
                }
            }
        }

        return new Automaton(dfaStates, alphabet, initialState, dfaFinalStates, dfaTransitions);
    }
}
