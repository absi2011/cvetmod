package cvetmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.powers.NoAttackPower;

public class RebuildKazdel extends AbstractCvetCard{
    public static final String ID = "cvetmod:RebuildKazdel";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Analyse.png";
    public static final int COST = 1;
    public static final int SECOND_COST = 1;
    public static final int BLOCK_GAIN = 25;
    public static final int BLOCK_REDUCE = 5;
    public static final int UPG_BLOCK = 5;
    public RebuildKazdel() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock  = BLOCK_GAIN;
        magicNumber = baseMagicNumber = BLOCK_REDUCE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new ApplyPowerAction(p, p, new NoAttackPower(p)));
    }

    @Override
    public void applyPowers() {
        int cnt = calcAttackCardsThisTurn();
        baseBlock -= cnt * magicNumber;
        super.applyPowers();
        baseBlock += cnt * magicNumber;
        isBlockModified = (baseBlock != block);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int cnt = calcAttackCardsThisTurn();
        baseBlock -= cnt * magicNumber;
        super.calculateCardDamage(mo);
        baseBlock += cnt * magicNumber;
        isBlockModified = (baseBlock != block);
    }

    int calcAttackCardsThisTurn()
    {
        int cnt = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == CardType.ATTACK) {
                cnt ++;
            }
        }
        return cnt;
    }



    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            initializeDescription();
        }
    }
}
