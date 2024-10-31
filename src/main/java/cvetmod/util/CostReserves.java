package cvetmod.util;

public class CostReserves {
    private static int curReserves = 0;
    public static void setReserves(int amount) {
        curReserves = amount;
        SecondCostEnergyOrb.secondVfxTimer = 2.0F;
        SecondCostEnergyOrb.fontScale = 2.0F;
    }

    public static void addReserves(int amount) {
        curReserves += amount;
        if (amount > 0) {
            SecondCostEnergyOrb.secondVfxTimer = 2.0F;
            SecondCostEnergyOrb.fontScale = 2.0F;
        }
    }

    public static void useReserves(int amount) {
        curReserves -= amount;
        if (curReserves < 0) {
            curReserves = 0;
        }
        if (amount > 0) {
            SecondCostEnergyOrb.fontScale = 2.0F;
        }
    }

    public static int reserveCount() {
        return curReserves;
    }

    public static void resetReserves() {
        curReserves = 0;
    }
}
