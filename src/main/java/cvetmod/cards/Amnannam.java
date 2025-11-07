package cvetmod.cards;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.cards.green.Reflex;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Amnannam extends AbstractCvetCard implements OnObtainCard {
    public static final String ID = "cvetmod:Amnannam";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Analyse.png";
    public static final int COST = -2;
    public static final int SECOND_COST = -2;
    public static final int UPG_COST = 0;
    public static final int UPG_SECOND_COST = 0;
    public Amnannam() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if ((cost == -2) || (secondCost == -2)) {
            cantUseMessage = CardCrawlGame.languagePack.getCardStrings(Reflex.ID).EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p,m);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
            upgradeBaseSecondCost(UPG_SECOND_COST);
            initializeDescription();
        }
    }

    public void onObtainCard() {
        AbstractDungeon.player.decreaseMaxHealth(9);
    }
}
