import java.io.Serializable;

public class Vote implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String candidate;

    public Vote(String candidate) {
        this.candidate = candidate;
    }

    public String getCandidate() {
        return candidate;
    }
}
