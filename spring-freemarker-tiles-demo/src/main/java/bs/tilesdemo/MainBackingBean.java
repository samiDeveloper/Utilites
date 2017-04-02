package bs.tilesdemo;

import java.io.Serializable;

public class MainBackingBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;

    public void init()
    {
        name = "initialized";
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
