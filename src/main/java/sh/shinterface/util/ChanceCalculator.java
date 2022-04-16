package sh.shinterface.util;

public final class ChanceCalculator {

    public static int factorial(int n) {
        if (n < 0) {
            return 1;
        }
        if (n == 0) {
            return 1;
        } else {
            return n * ChanceCalculator.factorial(n - 1);
        }
    }

    public static int nCk(int n, int k) {
        if (n == k) {
            return 1;
        }
        int teller = 1;
        for (int i = 0; i < k; i++) {
            teller *= n - i;
        }
        return teller / ChanceCalculator.factorial(k);
    }
}
