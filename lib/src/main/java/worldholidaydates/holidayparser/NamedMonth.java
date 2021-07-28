package worldholidaydates.holidayparser;

/**
 * An interface for month, whose names are different on different calendar systems 
 */
public interface NamedMonth {
    public int getValue();

    public String getName();

    @Override
    public String toString();
}
