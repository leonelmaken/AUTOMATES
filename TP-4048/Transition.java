import java.util.Objects;

public class Transition {
    private State fromState;
    private State toState;
    private char symbol;

    public Transition(State fromState, State toState, char symbol) {
        this.fromState = fromState;
        this.toState = toState;
        this.symbol = symbol;
    }

    // Getters and setters

    public State getFromState() {
        return fromState;
    }

    public void setFromState(State fromState) {
        this.fromState = fromState;
    }

    public State getToState() {
        return toState;
    }

    public void setToState(State toState) {
        this.toState = toState;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "fromState=" + fromState +
                ", toState=" + toState +
                ", symbol=" + symbol +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return symbol == that.symbol &&
                Objects.equals(fromState, that.fromState) &&
                Objects.equals(toState, that.toState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromState, toState, symbol);
    }
}
