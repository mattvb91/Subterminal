package mavonie.subterminal.Models.Api;

import java.util.ArrayList;
import java.util.List;

public class Exits {

    private List<mavonie.subterminal.Models.Exit> exits = new ArrayList<>();

    /**
     * @return The exits
     */
    public List<mavonie.subterminal.Models.Exit> getExits() {
        return exits;
    }

    /**
     * @param exits The exits
     */
    public void setExits(List<mavonie.subterminal.Models.Exit> exits) {
        this.exits = exits;
    }
}