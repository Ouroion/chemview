package org.chemview.api.periodicTableEndpoint;

public class element {
    private final String name;
    private final String symbol;

        public element(String name, String symbol){
            this.name = name;
            this.symbol = symbol;
        }

        public String getName(){
            return name;
        }

        public String getSymbol(){
            return symbol;
        }
}
