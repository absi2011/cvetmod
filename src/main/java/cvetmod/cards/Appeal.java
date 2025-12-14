package cvetmod.cards;

import basemod.helpers.CardPowerTip;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.AppealAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.red.Whirlwind;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;
import cvetmod.powers.AppealPower;

public class Appeal extends AbstractCvetCard {
    public static final String ID = "cvetmod:Appeal";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Analyse.png";
    public static final int COST = -1;
    public static final int SECOND_COST = 0;
    public static final int ATT_DAMAGE = 11;
    public static final int UPG_DAMAGE = 4;
    public Appeal() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = ATT_DAMAGE;
        isMultiDamage = true;
        // CardPowerTip(this, EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AppealAction(p, multiDamage, damageTypeForTurn, freeToPlayOnce, energyOnUse));
        addToBot(new ChangeStanceAction(WrathStance.STANCE_ID));
        addToBot(new ApplyPowerAction(p, p, new AppealPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeDamage(UPG_DAMAGE);
            upgradeName();
        }
    }
}
