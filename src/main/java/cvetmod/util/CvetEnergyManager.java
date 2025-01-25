package cvetmod.util;

import com.megacrit.cardcrawl.core.EnergyManager;

public class CvetEnergyManager extends EnergyManager {
    public int secondEnergy;
    public int secondEnergyMaster;
    public CvetEnergyManager(int e,int e2) {
        super(e);
        secondEnergyMaster = e2;
    }

    @Override
    public void prep() {
        super.prep();
        secondEnergy = secondEnergyMaster;
        CostReserves.resetReserves();
    }

    @Override
    public void recharge() {
        super.recharge();
        CostReserves.setReserves(secondEnergy);
    }
}
