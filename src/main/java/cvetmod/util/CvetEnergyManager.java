package cvetmod.util;

import com.megacrit.cardcrawl.core.EnergyManager;

public class CvetEnergyManager extends EnergyManager {
    public int secondEnergy;
    public int secondEnergyMaster;
    public CvetEnergyManager(int e,int e2) {
        super(e);
        secondEnergyMaster = e2;    // Not used now.
    }

    @Override
    public void prep() {
        super.prep();
        secondEnergy = energyMaster;
        CostReserves.resetReserves();
        CostReserves.setReserves(secondEnergy);
    }

    @Override
    public void recharge() {
        super.recharge();
        CostReserves.setReserves(secondEnergy);
    }
}
