package il.ac.shenkar.mobile.todoApp;
/**
 * IMPORTACY ENUM
 * */
public enum Importancy 
{
	NONE(0), LOW(1), MEDIUM(2), HIGH(3);
    private final int value;
    private Importancy(int value)
    {
        this.value = value;
    }
    //get the enum value
    public int getValue() 
    {
        return value;
    }
}
