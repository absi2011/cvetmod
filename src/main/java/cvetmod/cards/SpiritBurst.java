package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import cvetmod.actions.GainSecondEnergyAction;
import cvetmod.powers.SpiritBurstPower;

public class SpiritBurst extends AbstractCvetCard {
    public static final String ID = "cvetmod:SpiritBurst";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 0;
    public static final int ATK_DMG = 2;
    public static final int ATK_TIMES = 8;
    public static final int UPG_DMG = 1;
    public SpiritBurst() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF);
        damage = baseDamage = ATK_DMG;
        magicNumber = baseMagicNumber = ATK_TIMES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new AttackDamageRandomEnemyAction(this));
        }
        addToBot(new ApplyPowerAction(p, p, new SpiritBurstPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DMG);
            initializeDescription();
        }
    }
}
