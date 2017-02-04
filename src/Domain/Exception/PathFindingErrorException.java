package Domain.Exception;

/**
 * Created by IanCJ on 2/4/2017.
 */
public class PathFindingErrorException  extends Exception{

    String errorMessage;

    public PathFindingErrorException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
