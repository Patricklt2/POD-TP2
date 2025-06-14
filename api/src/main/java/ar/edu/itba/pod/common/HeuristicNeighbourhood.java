package ar.edu.itba.pod.common;

public enum HeuristicNeighbourhood {
    // One letter matches
    QUEENS(0, "QUEENS"),
    UPTOWN(0, "UPTOWN"),
    KENWOOD(0, "KENWOOD"),
    JEFFERSON_PARK(0, "JEFFERSON PARK"),
    IRVING_PARK(0, "IRVING PARK"),
    STATEN_ISLAND(1, "STATEN ISLAND"),
    MCKINLEY_PARK(1, "MCKINLEY PARK"),
    ASHBURN(1, "ASHBURN"),
    HYDE_PARK(1, "HYDE PARK"),
    ENGLEWOOD(1, "ENGLEWOOD"),
    ARCHER_HEIGHTS(2, "ARCHER HEIGHTS"),
    LOOP(3, "LOOP"),
    NEW_CITY(3, "NEW CITY"),
    EDISON_PARK(3, "EDISON PARK"),
    BRONX(4, "BRONX"),
    BROOKLYN(4, "BROOKLYN"),
    BRIDGEPORT(4, "BRIDGEPORT"),
    LAKE_VIEW(5, "LAKE VIEW"),
    ALBANY_PARK(5, "ALBANY PARK"),
    RIVERDALE(5, "RIVERDALE"),
    GREATER_GRAND_CROSSING(6, "GREATER GRAND CROSSING"),
    CHATHAM(6, "CHATHAM"),
    WEST_PULLMAN(6, "WEST PULLMAN"),
    GRAND_BOULEVARD(6, "GRAND BOULEVARD"),
    NEAR_SOUTH_SIDE(7, "NEAR SOUTH SIDE"),
    HEGEWISCH(7, "HEGEWISCH"),
    LOGAN_SQUARE(7, "LOGAN SQUARE"),
    SOUTH_CHICAGO(8, "SOUTH CHICAGO"),
    ARMOUR_SQUARE(8, "ARMOUR SQUARE"),
    BELMONT_CRAGIN(8, "BELMONT CRAGIN"),
    NORTH_PARK(9, "NORTH PARK"),
    LINCOLN_SQUARE(9, "LINCOLN SQUARE"),
    WEST_ELSDON(9, "WEST ELSDON"),
    MOUNT_GREENWOOD(11, "MOUNT GREENWOOD"),
    WASHINGTON_PARK(11, "WASHINGTON PARK"),
    WEST_ENGLEWOOD(11, "WEST ENGLEWOOD"),
    GARFIELD_RIDGE(11, "GARFIELD RIDGE"),
    NEAR_WEST_SIDE(11, "NEAR WEST SIDE"),
    CALUMET_HEIGHTS(12, "CALUMET HEIGHTS"),
    WASHINGTON_HEIGHTS(13, "WASHINGTON HEIGHTS"),
    AUBURN_GRESHAM(13, "AUBURN GRESHAM"),
    // Multi-letter matches (using python and itertools.combinations)
    AUSTIN(1, 2, 3, "AUSTIN"),
    AVALON_PARK(0, 1, 6, "AVALON PARK"),
    AVONDALE(1, 2, 4, "AVONDALE"),
    BEVERLY(2, 6, "BEVERLY"),
    BRIGHTON_PARK(0, 2, 4, "BRIGHTON PARK"),
    BURNSIDE(0, 1, 5, "BURNSIDE"),
    CHICAGO_LAWN(0, 9, "CHICAGO LAWN"),
    DOUGLAS(0, 2, 6, "DOUGLAS"),
    DUNNING(0, 1, 2, "DUNNING"),
    EAST_GARFIELD_PARK(1, 2, 7, "EAST GARFIELD PARK"),
    EDGEWATER(0, 1, 4, "EDGEWATER"),
    FOREST_GLEN(0, 5, "FOREST GLEN"),
    FULLER_PARK(0, 1, "FULLER PARK"),
    GAGE_PARK(0, 2, 4, "GAGE PARK"),
    HERMOSA(0, 1, 3, "HERMOSA"),
    HUMBOLDT_PARK(2, 3, "HUMBOLDT PARK"),
    LINCOLN_PARK(0, 1, 7, "LINCOLN PARK"),
    LOWER_WEST_SIDE(0, 1, 2, "LOWER WEST SIDE"),
    MANHATTAN(0, 1, 3, "MANHATTAN"),
    MONTCLARE(0, 3, 5, "MONTCLARE"),
    MORGAN_PARK(0, 2, 6, "MORGAN PARK"),
    NEAR_NORTH_SIDE(1, 4, 8, "NEAR NORTH SIDE"),
    NORTH_CENTER(0, 1, 5, "NORTH CENTER"),
    NORTH_LAWNDALE(0, 2, 5, "NORTH LAWNDALE"),
    NORWOOD_PARK(0, 3, 7, "NORWOOD PARK"),
    OAKLAND(0, 2, 4, "OAKLAND"),
    PORTAGE_PARK(0, 5, "PORTAGE PARK"),
    ROGERS_PARK(0, 4, 6, "ROGERS PARK"),
    SOUTH_DEERING(0, 5, 9, "SOUTH DEERING"),
    SOUTH_LAWNDALE(0, 1, 5, "SOUTH LAWNDALE"),
    SOUTH_SHORE(0, 5, 6, "SOUTH SHORE"),
    WEST_GARFIELD_PARK(0, 7, "WEST GARFIELD PARK"),
    WEST_RIDGE(0, 4, 7, "WEST RIDGE"),
    WEST_TOWN(1, 5, 6, "WEST TOWN"),
    WOODLAWN(0, 1, 4, "WOODLAWN"),
    CLEARING(0, 1, 2, 6, "CLEARING"),
    EAST_SIDE(0, 1, 5, "EAST SIDE"),
    OHARE(1, 2, 3, 4, "OHARE"),
    PULLMAN(0, 1, "PULLMAN"),
    ROSELAND(0, 2, "ROSELAND"),
    WEST_LAWN(0, 5, "WEST LAWN");

    private final int[] indexs;
    private final char[] characters;

    HeuristicNeighbourhood(final int i, final String s) {
        this.indexs = new int[] { i };
        this.characters = new char[] { s.charAt(i) };
    }

    HeuristicNeighbourhood(final int i, final int j, final String s) {
        this.indexs = new int[] { i, j };
        this.characters = new char[] { s.charAt(i), s.charAt(j) };
    }

    HeuristicNeighbourhood(final int i, final int j, final int k, final String s) {
        this.indexs = new int[] { i, j, k };
        this.characters = new char[] { s.charAt(i), s.charAt(j), s.charAt(k) };
    }

    HeuristicNeighbourhood(final int i, final int j, final int k, final int l, final String s) {
        this.indexs = new int[] { i, j, k, l };
        this.characters = new char[] { s.charAt(i), s.charAt(j), s.charAt(k), s.charAt(l) };
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
