package cvetmod.cards.special;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.vfx.OriginiumEffect;

public class Originium extends AbstractCard {
    public static final String ID = "cvetmod:Originium";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = -2;
    public static CardGroup originium = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public Originium() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void onRemoveFromMasterDeck() {
        AbstractDungeon.effectsQueue.add(new OriginiumEffect(new Originium(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }

    public void triggerOnExhaust() {
        addToBot(new MakeTempCardInHandAction(makeCopy()));
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if ((c.type != CardType.POWER) && (!c.exhaust)) {
            // TODO: originium.addToBottom(c);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Originium();
    }

    @Override
    public void upgrade() {
    }
}
