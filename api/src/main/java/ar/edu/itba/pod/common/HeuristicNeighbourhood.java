package ar.edu.itba.pod.common;

public enum HeuristicNeighbourhood {
    // One letter matches
    QUEENS("QUEENS", 0),
    UPTOWN("UPTOWN", 0),
    KENWOOD("KENWOOD", 0),
    JEFFERSON_PARK("JEFFERSON PARK", 0),
    IRVING_PARK("IRVING PARK", 0),
    STATEN_ISLAND("STATEN ISLAND", 1),
    MCKINLEY_PARK("MCKINLEY PARK", 1),
    ASHBURN("ASHBURN", 1),
    HYDE_PARK("HYDE PARK", 1),
    ENGLEWOOD("ENGLEWOOD", 1),
    ARCHER_HEIGHTS("ARCHER HEIGHTS", 2),
    LOOP("LOOP", 3),
    NEW_CITY("NEW CITY", 3),
    EDISON_PARK("EDISON PARK", 3),
    BRONX("BRONX", 4),
    BROOKLYN("BROOKLYN", 4),
    BRIDGEPORT("BRIDGEPORT", 4),
    LAKE_VIEW("LAKE VIEW", 5),
    ALBANY_PARK("ALBANY PARK", 5),
    RIVERDALE("RIVERDALE", 5),
    GREATER_GRAND_CROSSING("GREATER GRAND CROSSING", 6),
    CHATHAM("CHATHAM", 6),
    WEST_PULLMAN("WEST PULLMAN", 6),
    GRAND_BOULEVARD("GRAND BOULEVARD", 6),
    NEAR_SOUTH_SIDE("NEAR SOUTH SIDE", 7),
    HEGEWISCH("HEGEWISCH", 7),
    LOGAN_SQUARE("LOGAN SQUARE", 7),
    SOUTH_CHICAGO("SOUTH CHICAGO", 8),
    ARMOUR_SQUARE("ARMOUR SQUARE", 8),
    BELMONT_CRAGIN("BELMONT CRAGIN", 8),
    NORTH_PARK("NORTH PARK", 9),
    LINCOLN_SQUARE("LINCOLN SQUARE", 9),
    WEST_ELSDON("WEST ELSDON", 9),
    MOUNT_GREENWOOD("MOUNT GREENWOOD", 11),
    WASHINGTON_PARK("WASHINGTON PARK", 11),
    WEST_ENGLEWOOD("WEST ENGLEWOOD", 11),
    GARFIELD_RIDGE("GARFIELD RIDGE", 11),
    NEAR_WEST_SIDE("NEAR WEST SIDE", 11),
    CALUMET_HEIGHTS("CALUMET HEIGHTS", 12),
    WASHINGTON_HEIGHTS("WASHINGTON HEIGHTS", 13),
    AUBURN_GRESHAM("AUBURN GRESHAM", 13),
    // Two letters matches
    OHARE("OHARE", 0, 1),
    PULLMAN("PULLMAN", 0, 1),
    AUSTIN("AUSTIN", 1, 2),
    AVALON_PARK("AVALON PARK", 1, 2),
    AVONDALE("AVONDALE", 1, 2),
    BEVERLY("BEVERLY", 1, 2),
    BRIGHTON_PARK("BRIGHTON PARK", 2, 3),
    // Using python and itertools.combinations
    BURNSIDE("BURNSIDE", 0, 1, 5),
    CHICAGO_LAWN("CHICAGO LAWN", 0, 9),
    DOUGLAS("DOUGLAS", 0, 2, 6),
    DUNNING("DUNNING", 0, 1, 2),
    EAST_GARFIELD_PARK("EAST GARFIELD PARK", 1, 2, 7),
    EDGEWATER("EDGEWATER", 0, 1, 4),
    FOREST_GLEN("FOREST GLEN", 0, 5),
    FULLER_PARK("FULLER PARK", 0, 1),
    GAGE_PARK("GAGE PARK", 0, 2, 4),
    HERMOSA("HERMOSA", 0, 1, 3),
    HUMBOLDT_PARK("HUMBOLDT PARK", 2, 3),
    LINCOLN_PARK("LINCOLN PARK", 0, 1, 7),
    LOWER_WEST_SIDE("LOWER WEST SIDE", 0, 1, 2),
    MANHATTAN("MANHATTAN", 0, 1, 3),
    MONTCLARE("MONTCLARE", 0, 3, 5),
    MORGAN_PARK("MORGAN PARK", 0, 2, 6),
    NEAR_NORTH_SIDE("NEAR NORTH SIDE", 1, 4, 8),
    NORTH_CENTER("NORTH CENTER", 0, 1, 5),
    NORTH_LAWNDALE("NORTH LAWNDALE", 0, 2, 5),
    NORWOOD_PARK("NORWOOD PARK", 0, 3, 7),
    OAKLAND("OAKLAND", 0, 2, 4),
    PORTAGE_PARK("PORTAGE PARK", 0, 5),
    ROGERS_PARK("ROGERS PARK", 0, 4, 6),
    SOUTH_DEERING("SOUTH DEERING", 0, 5, 9),
    SOUTH_LAWNDALE("SOUTH LAWNDALE", 0, 1, 5),
    SOUTH_SHORE("SOUTH SHORE", 0, 5, 6),
    WEST_GARFIELD_PARK("WEST GARFIELD PARK", 0, 7),
    WEST_RIDGE("WEST RIDGE", 0, 4, 7),
    WEST_TOWN("WEST TOWN", 1, 5, 6),
    WOODLAWN("WOODLAWN", 0, 1, 4),
    CLEARING("CLEARING", 0, 1, 2, 6),
    EAST_SIDE("EAST SIDE", 0, 1, 5),
    ROSELAND("ROSELAND", 0, 2),
    WEST_LAWN("WEST LAWN", 0, 5);

    private final int[] indexs;
    private final char[] characters;

    /**
     * Constructor for HeuristicNeighbourhood.
     *
     * @param s the name of the neighbourhood
     * @param ix the indices of the characters in the name that
     * are used for matching, sorted in ascending order
     */
    HeuristicNeighbourhood(final String s, final int... ix) {
        this.indexs = ix;
        this.characters = new char[ix.length];
        for (int j = 0; j < ix.length; j++) {
            this.characters[j] = s.charAt(ix[j]);
        }
    }

    public String getCity() {
        return this == MANHATTAN || this == BRONX || this == BROOKLYN || this == STATEN_ISLAND || this == QUEENS
                ? "NYC"
                : "CHI";
    }

    public boolean hequals(final String s) {
        for (int test = 0; test < indexs.length; test++) {
            int i = indexs[test];
            char c = characters[test];

            if (i >= s.length() || s.charAt(i) != c) {
                return false;
            }
        }

        return true;
    }
}
