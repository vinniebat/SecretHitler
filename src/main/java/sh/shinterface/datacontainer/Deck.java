package sh.shinterface.datacontainer;

public record Deck(int allLib, int allFasc, int restLib, int restFasc) {

    public double predictChance(int numberOfLibs) {
        double teller = 0;
        double restTotal = restLib + restFasc;
        double noemer = restTotal * (restTotal - 1) * (restTotal - 2);
        if (numberOfLibs == 0) {
            teller = restFasc * (restFasc - 1) * (restFasc - 2);
        } else if (numberOfLibs == 1) {
            teller = 3 * restFasc * (restFasc - 1) * restLib;
        } else if (numberOfLibs == 2) {
            teller = 3 * restFasc * restLib * (restLib - 1);
        } else if (numberOfLibs == 3) {
            teller = restLib * (restLib - 1) * (restLib - 2);
        }
        return teller / noemer;
    }
}
